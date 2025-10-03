package com.varible.adstriggercompose.admangers

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.varible.adstriggercompose.di.ActivityContextHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.google.android.gms.ads.rewarded.RewardedAd
import com.varible.adstriggercompose.R
import javax.inject.Inject
import kotlin.math.pow

class AdMobManager @Inject constructor(private val activity: ActivityContextHolder) :
    AdMangerConsent() {

    private val lifeCycleScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val tag = "AdMobManager"
    private var mRewardAd: RewardedAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    private var ssvo: ServerSideVerificationOptions? = null

    private var showAdResults = MutableSharedFlow<Boolean>()

    private var retryAttempt = 0
    private var retryInterAttempt = 0

    private var adError: LoadAdError? = null

    init {
        Log.e(tag, "getContext:- ${activity.getContext()}")
        activity.getContext()?.let {
            Log.e(tag, "initAdmangerModule:")
            initConsentManager(it)
        }
    }


    override fun init() {
        CoroutineScope(Dispatchers.Default).launch {
            activity.getContext()?.let { context ->
                MobileAds.initialize(context) {
                    Log.d("init","admob initialized $it")
                    MainScope().launch {
                        initAds()
                        loadInter()
//                        addAdsInspector(context)
                    }
                }
            }
        }
    }

    override fun initAds() {
        Log.e(tag, "initAds:$adError")
        if(mRewardAd == null) loadInter()
        if (mRewardAd == null) {
            val ctx = activity.getContext() ?: return
            val adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                ctx, ctx.getString(R.string.admob_app_id), adRequest,
                object : RewardedAdLoadCallback() {

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        this@AdMobManager.adError = adError
                        mRewardAd = null
                        retryAttempt++
                        val delayMillis =
                            TimeUnit.SECONDS.toMillis(
                                2.0.pow(3.0.coerceAtMost(retryAttempt.toDouble())).toLong()
                            )
                        lifeCycleScope.launch {
                            delay(delayMillis)
                            initAds()
                        }
                        Log.e(tag, "onAdFailedToLoadAdmob:$adError")
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        adError = null
                        mRewardAd = rewardedAd
                        mRewardAd?.setServerSideVerificationOptions(ssvo)
                        retryAttempt = 0
                        showAd()
                        Log.e(tag, "onAdLoadedAdmob:$rewardedAd")
                    }
                }

            )

        }
    }

    fun loadInter() {
        Log.d(tag, "Loading Interstitial Ad")
        val ctx = activity.getContext() ?: return
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            ctx,
            ctx.getString(R.string.admob_interstitial_app_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(tag, "Ad failed to load $adError")
                    mInterstitialAd = null
                    val delayMillis =
                        TimeUnit.SECONDS.toMillis(
                            2.0.pow(3.0.coerceAtMost(retryInterAttempt.toDouble())).toLong()
                        )
                    lifeCycleScope.launch {
                        delay(delayMillis)
                        loadInter()
                    }
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(tag, "inter loaded ${interstitialAd.responseInfo}")
                    mInterstitialAd = interstitialAd
                    retryInterAttempt = 0
                }
            })
    }

    override fun setUserId(userId: Long) {
        ssvo =
            ServerSideVerificationOptions
                .Builder()
                .setUserId(userId.toString())
                .build()
    }

    override fun showAd() {
        val ctx = activity.getContext()
        if (ctx != null && mRewardAd != null) {
            Log.d(tag, "showing ad")
            mRewardAd?.fullScreenContentCallback = rewardedAdCallback
            mRewardAd?.show(ctx) { Log.d(tag,"reward earned? $it") }
        }else{
            Log.d(tag, "showing ad error:- $ctx $mRewardAd")
            initAds()
        }
    }

    override fun getAdResultFlow(): SharedFlow<Boolean> = showAdResults

    override fun showInter() {
        val ctx = activity.getContext()
        if (ctx != null && mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = interAdCallback
            mInterstitialAd?.show(ctx)
        }
    }


    private fun adTestIds() {
        val configuration = RequestConfiguration
            .Builder()
            .setTestDeviceIds(listOf("3FD24E333E15506B96AB307A0465A6F6"))
            .build()
        MobileAds.setRequestConfiguration(configuration)

    }

    private fun addAdsInspector(context: Context) {
        adTestIds()
        MobileAds.openAdInspector(context) {
        }
    }

    private val rewardedAdCallback =
        object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d(tag, "Ad was clicked rewardAd.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(tag, "Ad dismissed fullscreen content rewardAd.")
                lifeCycleScope.launch { showAdResults.emit(true) }
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(tag, "Ad failed to show fullscreen content rewardAd $adError")
                mRewardAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                mRewardAd = null
                Log.d(tag, "Ad showed fullscreen content rewardAd.")
                initAds()
            }

            override fun onAdImpression() {
                Log.d(tag, "Ad recorded an impression rewardAd.")
            }
        }

    private val interAdCallback = object : FullScreenContentCallback() {
        override fun onAdClicked() {
            // Called when a click is recorded for an ad.
            Log.d(tag, "Ad was clicked.")
        }

        override fun onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
            // Set the ad reference to null so you don't show the ad a second time.
            Log.d(tag, "Ad dismissed fullscreen content.")
            mInterstitialAd = null
            loadInter()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            // Called when ad fails to show.
            Log.d(tag, "Ad failed to show fullscreen content $adError")
            mInterstitialAd = null
        }

        override fun onAdImpression() {
            // Called when an impression is recorded for an ad.
            Log.d(tag, "Ad recorded an impression.")
        }

        override fun onAdShowedFullScreenContent() {
            // Called when ad is shown.
            Log.d(tag, "Ad showed fullscreen content.")
        }
    }


}