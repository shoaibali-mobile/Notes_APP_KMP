package com.shoaib.notes_app_kmp

import android.content.Context

actual class PlatformContext(val context: Context)

lateinit var appContext: Context
    private set

fun initAppContext(context: Context) {
    appContext = context
}

actual fun getPlatformContext(): PlatformContext {
    return PlatformContext(appContext)
}

actual fun isPlatformContextInitialized(): Boolean {
    return ::appContext.isInitialized
}

