package com.shoaib.notes_app_kmp.util

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.shoaib.notes_app_kmp.BuildConfig
import com.shoaib.notes_app_kmp.appContext
import com.shoaib.notes_app_kmp.util.logD

/**
 * Android implementation of RemoteConfigHelper using Firebase Remote Config.
 */
actual class RemoteConfigHelper {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private var isInitialized = false

    actual fun initialize() {
        if (isInitialized) return

        // Configure Remote Config settings
        val configSettings = FirebaseRemoteConfigSettings.Builder().apply {
            if (BuildConfig.DEBUG) {
                // In debug mode, use shorter fetch interval for testing
                minimumFetchIntervalInSeconds = 0L
                logD("RemoteConfigHelper", "Debug mode: Remote Config fetch interval set to 0 seconds")
            } else {
                // In release mode, use default fetch interval (12 hours)
                minimumFetchIntervalInSeconds = 3600L
            }
        }.build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        // Set default values (optional, but recommended)
        val defaultValues = mapOf(
            "bottom_bar_config" to """{"home": true, "settings": true, "profile": true}"""
        )
        remoteConfig.setDefaultsAsync(defaultValues)

        isInitialized = true
        logD("RemoteConfigHelper", "Remote Config initialized")
    }

    actual suspend fun fetch(onComplete: (Boolean) -> Unit) {
        if (!isInitialized) {
            logD("RemoteConfigHelper", "Remote Config not initialized, initializing now...")
            initialize()
        }

        try {
            remoteConfig.fetch().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    logD("RemoteConfigHelper", "Remote Config fetched successfully")
                    onComplete(true)
                } else {
                    logD("RemoteConfigHelper", "Remote Config fetch failed: ${task.exception?.message}")
                    onComplete(false)
                }
            }
        } catch (e: Exception) {
            logD("RemoteConfigHelper", "Remote Config fetch error: ${e.message}")
            onComplete(false)
        }
    }

    actual suspend fun activate(onComplete: (Boolean) -> Unit) {
        if (!isInitialized) {
            logD("RemoteConfigHelper", "Remote Config not initialized")
            onComplete(false)
            return
        }

        try {
            remoteConfig.activate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    logD("RemoteConfigHelper", "Remote Config activated successfully")
                    onComplete(true)
                } else {
                    logD("RemoteConfigHelper", "Remote Config activation failed: ${task.exception?.message}")
                    onComplete(false)
                }
            }
        } catch (e: Exception) {
            logD("RemoteConfigHelper", "Remote Config activation error: ${e.message}")
            onComplete(false)
        }
    }

    actual suspend fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        if (!isInitialized) {
            logD("RemoteConfigHelper", "Remote Config not initialized, initializing now...")
            initialize()
        }

        try {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val activated = task.result
                    logD("RemoteConfigHelper", "Remote Config fetch and activate completed. Activated: $activated")
                    onComplete(activated)
                } else {
                    logD("RemoteConfigHelper", "Remote Config fetch and activate failed: ${task.exception?.message}")
                    onComplete(false)
                }
            }
        } catch (e: Exception) {
            logD("RemoteConfigHelper", "Remote Config fetch and activate error: ${e.message}")
            onComplete(false)
        }
    }

    actual fun getString(key: String, defaultValue: String): String {
        if (!isInitialized) {
            logD("RemoteConfigHelper", "Remote Config not initialized, returning default value")
            return defaultValue
        }
        return remoteConfig.getString(key).takeIf { it.isNotEmpty() } ?: defaultValue
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        if (!isInitialized) {
            logD("RemoteConfigHelper", "Remote Config not initialized, returning default value")
            return defaultValue
        }
        return remoteConfig.getBoolean(key)
    }

    actual fun getLong(key: String, defaultValue: Long): Long {
        if (!isInitialized) {
            logD("RemoteConfigHelper", "Remote Config not initialized, returning default value")
            return defaultValue
        }
        return remoteConfig.getLong(key)
    }

    actual fun getDouble(key: String, defaultValue: Double): Double {
        if (!isInitialized) {
            logD("RemoteConfigHelper", "Remote Config not initialized, returning default value")
            return defaultValue
        }
        return remoteConfig.getDouble(key)
    }
}

actual fun createRemoteConfigHelper(): RemoteConfigHelper {
    return RemoteConfigHelper()
}

