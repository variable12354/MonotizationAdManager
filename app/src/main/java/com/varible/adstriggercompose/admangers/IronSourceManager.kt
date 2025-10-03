package com.varible.adstriggercompose.admangers

import android.util.Log
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener
import com.unity3d.mediation.LevelPlay
import com.unity3d.mediation.LevelPlayConfiguration
import com.unity3d.mediation.LevelPlayInitError
import com.unity3d.mediation.LevelPlayInitListener
import com.unity3d.mediation.LevelPlayInitRequest
import com.varible.adstriggercompose.R
import com.varible.adstriggercompose.di.ActivityContextHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class IronSourceManager @Inject constructor(
    private val activity: ActivityContextHolder
) : AdMangerConsent() {

    private val tag = "IronSourceManager"

    private val lifecycleScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var isNeedToShowOfferWallLater = false
    private val showAdResults = MutableSharedFlow<Boolean>()

    init {
        activity.getContext()?.let {
            initConsentManager(it)
        }
        Log.e(tag, "IronSourceManager:" )
    }

    override fun init() {
        initAds()
    }

    override fun initAds() {
        IronSource.setLevelPlayRewardedVideoListener(levelPlayRewardedListener)
        activity.getContext()?.let {

            val legacyAdFormats =
                listOf(LevelPlay.AdFormat.INTERSTITIAL, LevelPlay.AdFormat.REWARDED)
            val initRequest =
                LevelPlayInitRequest
                    .Builder(it.getString(R.string.iron_source_app_id))
                    .withLegacyAdFormats(legacyAdFormats)
                    .build()

            LevelPlay.init(
                it,
                initRequest,
                object : LevelPlayInitListener {
                    override fun onInitFailed(error: LevelPlayInitError) {
                    }

                    override fun onInitSuccess(configuration: LevelPlayConfiguration) {
                        Log.e(tag, "IronSourceManager:${configuration.toString()} ", )
                    }
                },
            )

        }
    }

    override fun setUserId(userId: Long) {
        IronSource.setUserId(userId.toString())
    }

    override fun showAd() {
        Log.e("tag", "showAd:${IronSource.isRewardedVideoAvailable()} ", )
        isNeedToShowOfferWallLater = false
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.showRewardedVideo()
        } else {
            isNeedToShowOfferWallLater = true
            lifecycleScope.launch {
                showAdResults.emit(false)
            }
        }
    }

    override fun getAdResultFlow(): SharedFlow<Boolean> = showAdResults

    private val levelPlayRewardedListener = object : LevelPlayRewardedVideoListener {
        override fun onAdOpened(p0: AdInfo?) {
            Log.d(tag, "IRON_SOURCE onAdOpened")
        }

        override fun onAdShowFailed(
            p0: IronSourceError?,
            p1: AdInfo?,
        ) {
            Log.d(tag, "IRON_SOURCE onAdShowFailed")
        }

        override fun onAdClicked(
            p0: Placement?,
            p1: AdInfo?,
        ) {
            Log.d(tag, "IRON_SOURCE onAdClicked")
        }

        override fun onAdRewarded(
            p0: Placement?,
            p1: AdInfo?,
        ) {
            Log.d(tag, "IRON_SOURCE onAdRewarded")
            lifecycleScope.launch {
                showAdResults.emit(true)
            }
        }

        override fun onAdClosed(p0: AdInfo?) {
            Log.d(tag, "IRON_SOURCE onAdClosed")
            lifecycleScope.launch {
                showAdResults.emit(true)
            }
        }

        override fun onAdAvailable(p0: AdInfo?) {
            Log.d(tag, "IRON_SOURCE onAdAvailable")
            if (isNeedToShowOfferWallLater) showAd()
        }

        override fun onAdUnavailable() {
            Log.d(tag, "IRON_SOURCE onAdAvailable")
        }
    }


}