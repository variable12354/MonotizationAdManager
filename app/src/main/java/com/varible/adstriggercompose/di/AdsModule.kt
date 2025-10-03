package com.varible.adstriggercompose.di

import android.app.Application
import com.varible.adstriggercompose.admangers.AdManger
import com.varible.adstriggercompose.admangers.AdMobManager
import com.varible.adstriggercompose.admangers.ApplovinManager
import com.varible.adstriggercompose.admangers.IronSourceManager
import com.varible.adstriggercompose.admangers.TapjoyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


const val AD_MANAGER_APPLOVIN = "applovin"

const val AD_MANAGER_ADMOB = "admob"

const val AD_TAP_JOY = "offerWall"

const val AD_MANAGER_IRON_SOURCE = "ironSource"


@Module
@InstallIn(SingletonComponent::class)
class AdsModule {

/*    @Provides
    @Singleton
    fun provideContextManager(): ActivityContextHolder = ActivityContextHolder()*/

    @Provides
    @Singleton
    @Named(AD_MANAGER_ADMOB)
    fun provideAdMobManager(contextHolder: ActivityContextHolder): AdManger =
        AdMobManager(contextHolder)

    @Provides
    @Singleton
    @Named(AD_MANAGER_IRON_SOURCE)
    fun provideAdIronSourceManager(contextHolder: ActivityContextHolder): AdManger =
        IronSourceManager(contextHolder)

    @Provides
    @Singleton
    @Named(AD_MANAGER_APPLOVIN)
    fun provideAdApplovinManager(contextHolder: ActivityContextHolder): AdManger =
        ApplovinManager(contextHolder)

    @Provides
    @Singleton
    @Named(AD_TAP_JOY)
    fun provideOfferTapJoy(
        application: Application,
        contextHolder: ActivityContextHolder,
    ): AdManger = TapjoyManager(application,contextHolder)



}

