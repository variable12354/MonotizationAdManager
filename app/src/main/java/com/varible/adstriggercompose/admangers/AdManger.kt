package com.varible.adstriggercompose.admangers

import kotlinx.coroutines.flow.SharedFlow

interface AdManger {

    fun initAds()

    fun setUserId(userId:Long)

    fun showAd()

    fun getAdResultFlow(): SharedFlow<Boolean>

    fun showInter() = Unit


}