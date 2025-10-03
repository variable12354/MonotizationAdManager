package com.varible.adstriggercompose.admangers

import android.content.Context
import android.util.Log
import com.tapjoy.TJActionRequest
import com.tapjoy.TJConnectListener
import com.tapjoy.TJError
import com.tapjoy.TJPlacement
import com.tapjoy.TJPlacementListener
import com.tapjoy.TJSetUserIDListener
import com.tapjoy.Tapjoy
import com.tapjoy.TapjoyConnectFlag
import com.varible.adstriggercompose.R
import com.varible.adstriggercompose.di.ActivityContextHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharedFlow
import java.util.Hashtable
import javax.inject.Inject

class TapjoyManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val activity: ActivityContextHolder, // holder for current Activity
) : AdManger {

    private var placement: TJPlacement? = null
    private var isDebuggable: Boolean = true
    private var userId: Long? = null
    private var isConnected: Boolean = false
    private var failureCount: Int = 0
    private val tag = "TapjoyManager"

    override fun initAds() {
        if (isDebuggable) Tapjoy.setDebugEnabled(true)
        connect()
    }

    private fun connect() {
        val connectFlags = Hashtable<String, Any>().apply {
            put(TapjoyConnectFlag.ENABLE_LOGGING, isDebuggable.toString())
            put(TapjoyConnectFlag.ALLOW_LEGACY_ID_FALLBACK, "true")
            userId?.takeIf { it != 0L }?.let {
                put(TapjoyConnectFlag.USER_ID, it.toString())
            }
        }

        Tapjoy.connect(
            context,
            context.getString(R.string.tapjoy_app_id), // replace with actual key
            connectFlags,
            object : TJConnectListener() {
                override fun onConnectSuccess() {
                    isConnected = true
                    Log.d(tag, "✅ Tapjoy connected successfully")
                    requestPlacement()
                }

                override fun onConnectFailure(code: Int, message: String?) {
                    isConnected = false
                    Log.e(tag, "❌ Tapjoy connect failed: $code $message")
                }
            }
        )
    }

    override fun setUserId(userId: Long) {
        if (!isConnected) return
        this.userId = userId

        Tapjoy.setUserID(userId.toString(), object : TJSetUserIDListener {
            override fun onSetUserIDSuccess() {
                Log.d(tag, "✅ UserID set successfully: $userId")
                requestPlacement() // preload after success
            }

            override fun onSetUserIDFailure(error: String?) {
                Log.e(tag, "❌ Failed to set UserID: $error")
            }
        })
    }

    private fun requestPlacement() {
        placement = Tapjoy.getPlacement(PLACEMENT_NAME, object : TJPlacementListener {
            override fun onRequestSuccess(p: TJPlacement?) {
                failureCount = 0
                Log.d(tag, "✅ Placement request success")
            }

            override fun onRequestFailure(p: TJPlacement?, error: TJError?) {
                failureCount++
                Log.e(tag, "❌ Placement request failed: ${error?.message}")
                if (failureCount < 2) p?.requestContent()
            }

            override fun onContentReady(p: TJPlacement?) {
                Log.d(tag, "✅ Content ready")
            }

            override fun onContentShow(p: TJPlacement?) {
                Log.d(tag, "ℹ️ Content shown")
            }

            override fun onContentDismiss(p: TJPlacement?) {
                Log.d(tag, "ℹ️ Content dismissed, reloading")
                p?.requestContent()
            }

            override fun onPurchaseRequest(p: TJPlacement?, req: TJActionRequest?, id: String?) {}
            override fun onRewardRequest(
                p: TJPlacement?,
                req: TJActionRequest?,
                currency: String?,
                amount: Int
            ) {
            }

            override fun onClick(p: TJPlacement?) {}
        })

        placement?.requestContent()
    }

    override fun showAd() {
        if (!isConnected) {
            connect()
            return
        }
        placement?.let { p ->
            if (p.isContentReady) {
                p.showContent()
            } else {
                p.requestContent()
                Log.w(tag, "⚠️ Content not ready, re-requesting")
            }
        } ?: run {
            Log.w(tag, "⚠️ No placement initialized, requesting now")
            requestPlacement()
        }
    }

    override fun getAdResultFlow(): SharedFlow<Boolean> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val PLACEMENT_NAME = "offerwall_android"
    }
}
