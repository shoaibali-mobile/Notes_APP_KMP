package com.shoaib.notes_app_kmp

import android.content.Context
import androidx.fragment.app.FragmentActivity

actual class PlatformContext(val context: Context)

lateinit var appContext: Context
    private set

// Store current Activity reference for biometric authentication
var currentActivity: FragmentActivity? = null
    private set

fun initAppContext(context: Context) {
    appContext = context
}

fun setCurrentActivity(activity: FragmentActivity?) {
    currentActivity = activity
}

