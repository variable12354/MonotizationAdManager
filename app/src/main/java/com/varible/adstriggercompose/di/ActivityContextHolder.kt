package com.varible.adstriggercompose.di

import android.app.Activity
import android.util.Log
import java.lang.ref.WeakReference

class ActivityContextHolder {

    private var wrContext: WeakReference<Activity> = WeakReference<Activity>(null)

    fun insertContext(context: Activity) {
        wrContext.clear()
        Log.d("ActivityContextHolder","insertContext $context")
        wrContext = WeakReference(context)
    }

    fun clearContext(context: Activity) {
        if (context == wrContext.get()) {
            wrContext.clear()
        }
    }


    fun getContext(): Activity? = wrContext.get()

}