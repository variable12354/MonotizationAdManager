package com.varible.adstriggercompose

import android.app.Application
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.makeopinion.cpxresearchlib.CPXResearch
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration
import com.makeopinion.cpxresearchlib.models.SurveyPosition
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initCpxResearch()
    }


    private var cpxResearch: CPXResearch? = null

    fun cpxResearch(): CPXResearch = cpxResearch!!
    private fun initCpxResearch() {
        val userId = "0"

        val style =
            CPXStyleConfiguration(
                SurveyPosition.SideLeftNormal,
                "Earn up to 3 Coins in<br> 4 minutes with surveys",
                20,
                "#ffffff",
                "#ffaf20",
                true,
            )

        val config =
            CPXConfigurationBuilder(
                getString(R.string.cpx_app_id),
                userId,
                getString(R.string.cpx_hash_secret),
                style,
            ).build()

        val cpx = CPXResearch.init(config)
        cpxResearch = cpx
    }


}