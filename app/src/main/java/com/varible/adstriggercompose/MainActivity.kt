package com.varible.adstriggercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.varible.adstriggercompose.admangers.AdManger
import com.varible.adstriggercompose.di.AD_MANAGER_ADMOB
import com.varible.adstriggercompose.di.AD_MANAGER_APPLOVIN
import com.varible.adstriggercompose.di.AD_MANAGER_IRON_SOURCE
import com.varible.adstriggercompose.di.AD_TAP_JOY
import com.varible.adstriggercompose.di.ActivityContextHolder
import com.varible.adstriggercompose.ui.composeScreen.AdsProviderScreen
import com.varible.adstriggercompose.ui.theme.AdsTriggerComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var contextHolder: ActivityContextHolder

    @Inject
    @Named(AD_MANAGER_ADMOB)
    lateinit var adMobManager: AdManger

    @Inject
    @Named(AD_MANAGER_APPLOVIN)
    lateinit var adManagerApplovin: AdManger

    @Inject
    @Named(AD_MANAGER_IRON_SOURCE)
    lateinit var adManagerIronSource: AdManger

    @Inject
    @Named(AD_TAP_JOY)
    lateinit var offerWallManager: AdManger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        contextHolder.insertContext(this)

        adMobManager.initAds()
        adManagerIronSource.initAds()
        adManagerApplovin.initAds()
        offerWallManager.initAds()
        setContent {
            AdsTriggerComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    AdsProviderScreen(
                        innerPadding,
                        onShowAdMobInterstitial = ::showAdMobInterstitial,
                        onShowAdMobRewarded = ::showAdMobRewarded,
                        onShowIronSourceInterstitial = ::showIronSourceInterstitial,
                        onShowIronSourceRewarded = ::showIronSourceRewarded,
                        onShowApplovinInterstitial = ::showApplovinInterstitial,
                        onShowApplovinRewarded = ::showApplovinRewarded,
                        onOfferWall = ::showOfferWall,
                        onShowSurvey = ::showSurvey
                    )
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        contextHolder.insertContext(this)
    }

    override fun onDestroy() {
        contextHolder.clearContext(this)
        super.onDestroy()
    }

    fun showAdMobInterstitial() {
        adMobManager.showInter()
    }

    fun showAdMobRewarded() {
        adMobManager.showAd()
    }

    fun showIronSourceInterstitial() {}

    fun showIronSourceRewarded() {
        adManagerIronSource.showAd()
    }

    fun showApplovinInterstitial() {}

    fun showApplovinRewarded() {
        adManagerApplovin.showAd()
    }

    fun showOfferWall() {
        offerWallManager.showAd()
    }
    fun showSurvey(){

    }


}

