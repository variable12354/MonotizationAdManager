package com.varible.adstriggercompose.admangers

import android.R.attr.tag
import android.app.Activity
import com.applovin.sdk.AppLovinPrivacySettings
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.UserMessagingPlatform
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.metadata.MetaData

abstract class AdMangerConsent : AdManger {

    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager

    abstract fun init()

    fun initConsentManager(activity: Activity) {
        IronSource.setConsent(true)
        IronSource.setMetaData("do_not_sell", "true")

        AppLovinPrivacySettings.setHasUserConsent(true, activity)
        AppLovinPrivacySettings.setDoNotSell(true, activity)



        // Log the Mobile Ads SDK version.
        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(activity.applicationContext)
        googleMobileAdsConsentManager.gatherConsent(activity) { consentError ->
            if (consentError != null) {
                // Consent not obtained in current session.
            }

            if (googleMobileAdsConsentManager.canRequestAds) {
                init()
            }

            if (googleMobileAdsConsentManager.isPrivacyOptionsRequired) {
                // Regenerate the options menu or show option to include a privacy setting.
                UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
                    // Handle the error.
                }
            }
        }

        // This sample attempts to load ads using consent obtained in the previous session.
        if (googleMobileAdsConsentManager.canRequestAds) {
            init()
        }
    }

}