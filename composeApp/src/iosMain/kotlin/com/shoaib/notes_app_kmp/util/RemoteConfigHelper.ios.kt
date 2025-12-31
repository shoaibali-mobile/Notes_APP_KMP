package com.shoaib.notes_app_kmp.util

import com.shoaib.notes_app_kmp.util.AppLogger

/**
 * iOS placeholder implementation of RemoteConfigHelper.
 * TODO: Implement Firebase Remote Config for iOS when needed.
 */
actual class RemoteConfigHelper {

    private var isInitialized = false

    actual fun initialize() {
        isInitialized = true
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config placeholder initialized")
    }

    actual suspend fun fetch(onComplete: (Boolean) -> Unit) {
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config fetch - not implemented")
        onComplete(false)
    }

    actual suspend fun activate(onComplete: (Boolean) -> Unit) {
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config activate - not implemented")
        onComplete(false)
    }

    actual suspend fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config fetchAndActivate - not implemented")
        onComplete(false)
    }

    actual fun getString(key: String, defaultValue: String): String {
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config getString - returning default value")
        return defaultValue
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config getBoolean - returning default value")
        return defaultValue
    }

    actual fun getLong(key: String, defaultValue: Long): Long {
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config getLong - returning default value")
        return defaultValue
    }

    actual fun getDouble(key: String, defaultValue: Double): Double {
        AppLogger.d("RemoteConfigHelper", "iOS Remote Config getDouble - returning default value")
        return defaultValue
    }
}

actual fun createRemoteConfigHelper(): RemoteConfigHelper {
    return RemoteConfigHelper()
}

