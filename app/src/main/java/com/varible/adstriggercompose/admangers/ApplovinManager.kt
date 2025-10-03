package com.varible.adstriggercompose.admangers

import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.sdk.AppLovinSdk
import com.varible.adstriggercompose.R
import com.varible.adstriggercompose.di.ActivityContextHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.pow

class ApplovinManager @Inject constructor(
    private val activity: ActivityContextHolder
) : AdMangerConsent() {

    private val tag = "ApplovinManager"

    private val lifecycleScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var retryAttempt = 0.0

    private var rewardedAd: MaxRewardedAd? = null

    private val showAdResults = MutableSharedFlow<Boolean>()

    init {
        activity.getContext()?.let { initConsentManager(it) }
        Log.e(tag, "initAdsApplovinManager:")
    }

    private fun createRewardedAd() {
        Log.e(tag, "initAdsApplovinManager:Before ")
        if (activity.getContext() == null) return
        Log.e(tag, "initAdsApplovinManager:After ")
        rewardedAd =
            MaxRewardedAd.getInstance(activity.getContext()?.getString(R.string.applovin_app_id))
        rewardedAd?.setListener(applovinListener)
        loadAd()
    }

    private val applovinListener =
        object : MaxRewardedAdListener {
            override fun onAdLoaded(ad: MaxAd) {
                Log.d(tag, "onAdLoaded $ad")
                retryAttempt = 0.0
            }

            override fun onAdDisplayed(ad: MaxAd) {
                Log.d(tag, "onAdDisplayed $ad")
            }

            override fun onAdHidden(ad: MaxAd) {
                Log.d(tag, "onAdHidden $ad")
                loadAd()
                lifecycleScope.launch { showAdResults.emit(true) }
            }

            override fun onAdClicked(ad: MaxAd) {
                Log.d(tag, "onAdClicked $ad")
            }

            override fun onAdLoadFailed(
                adUnitId: String,
                error: MaxError,
            ) {
                Log.d(tag, "onAdLoadFailed $adUnitId, $error")
                retryAttempt++
                val delayMillis =
                    TimeUnit.SECONDS.toMillis(2.0.pow(3.0.coerceAtMost(retryAttempt)).toLong())
                lifecycleScope.launch {
                    delay(delayMillis)
                    loadAd()
                }
            }

            override fun onAdDisplayFailed(
                ad: MaxAd,
                error: MaxError,
            ) {
                Log.d(tag, "onAdDisplayFailed $ad, $error")
                loadAd()
            }

            override fun onUserRewarded(
                ad: MaxAd,
                reward: MaxReward,
            ) {
                Log.d(tag, "onUserRewarded $ad, reward $reward")
            }
        }

    private fun loadAd() {
        if (activity.getContext() == null) return
        Log.e(tag, "loadAd:Triggerd ")
        rewardedAd?.loadAd()
    }

    override fun init() {
        initAds()
    }

    override fun initAds() {
        Log.e(tag, "initAdsApplovinManager: ")
        createRewardedAd()
    }

    override fun setUserId(userId: Long) {
        val activity = activity.getContext() ?: return
        /* if (AppLovinSdk.getInstance(activity).configuration. != userId.toString()) {
             AppLovinSdk.getInstance(activity).userIdentifier = userId.toString()
         }*/
    }

    override fun showAd() {
        val activity = activity.getContext() ?: return
        Log.d(tag, "showing ad above:- $activity $rewardedAd")
        if (rewardedAd?.isReady == true) {
            rewardedAd?.showAd(activity)
        } else {
            initAds()
            lifecycleScope.launch { showAdResults.emit(false) }
        }
    }

    override fun getAdResultFlow(): SharedFlow<Boolean> = showAdResults

}