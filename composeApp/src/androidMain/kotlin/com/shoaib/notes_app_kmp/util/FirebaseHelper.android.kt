package com.shoaib.notes_app_kmp.util

import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.shoaib.notes_app_kmp.BuildConfig
import com.shoaib.notes_app_kmp.appContext
import com.shoaib.notes_app_kmp.util.logD

/**
 * Android implementation of FirebaseHelper.
 * Uses google-services.json for configuration (automatically loaded by Google Services plugin).
 */
actual class FirebaseHelper {
    
    private var isInitialized = false
    
    /**
     * Initialize Firebase with Android context.
     * The google-services.json file is automatically loaded by the Google Services plugin.
     */
    actual fun initialize() {
        if (isInitialized) return
        
        // Initialize Firebase if not already initialized
        // google-services.json is automatically processed by the plugin
        if (FirebaseApp.getApps(appContext).isEmpty()) {
            FirebaseApp.initializeApp(appContext)
            if (BuildConfig.DEBUG) {
                logD("FirebaseHelper", "Firebase App initialized")
            }
        }
        
        // Enable Analytics collection (enabled in ALL modes: debug and release)
        val analytics = Firebase.analytics
        analytics.setAnalyticsCollectionEnabled(true)
        if (BuildConfig.DEBUG) {
            logD("FirebaseHelper", "Analytics collection enabled")
        }
        
        // Get Crashlytics instance and enable it (enabled in ALL modes: debug and release)
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCrashlyticsCollectionEnabled(true)
        if (BuildConfig.DEBUG) {
            logD("FirebaseHelper", "Crashlytics collection enabled")
            logD("FirebaseHelper", "Firebase enabled in ALL modes (debug and release)")
            logD("FirebaseHelper", "Package: ${appContext.packageName}")
            logD("FirebaseHelper", "Firebase Apps count: ${FirebaseApp.getApps(appContext).size}")
        }
        
        isInitialized = true
    }
    
    actual fun isInitialized(): Boolean {
        return isInitialized && FirebaseApp.getApps(appContext).isNotEmpty()
    }
    
    actual fun logCrashlytics(message: String) {
        if (!isInitialized) return
        FirebaseCrashlytics.getInstance().log(message)
    }
    
    actual fun recordException(throwable: Throwable) {
        if (!isInitialized) return
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
    
    actual fun setCustomKey(key: String, value: String) {
        if (!isInitialized) return
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }
    
    actual fun setCustomKey(key: String, value: Long) {
        if (!isInitialized) return
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }
    
    actual fun setUserId(userId: String) {
        if (!isInitialized) return
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }
    
    actual fun setCollectionEnabled(enabled: Boolean) {
        if (!isInitialized) return
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(enabled)
    }
    
    // Analytics methods
    actual fun logEvent(eventName: String, parameters: Map<String, Any>?) {
        if (!isInitialized) return
        val analytics = Firebase.analytics
        
        // Ensure analytics is enabled (enabled in ALL modes: debug and release)
        analytics.setAnalyticsCollectionEnabled(true)
        
        if (parameters != null) {
            val bundle = android.os.Bundle().apply {
                parameters.forEach { (key, value) ->
                    when (value) {
                        is String -> putString(key, value)
                        is Long -> putLong(key, value)
                        is Int -> putInt(key, value)
                        is Double -> putDouble(key, value)
                        is Float -> putFloat(key, value)
                        is Boolean -> putBoolean(key, value)
                        else -> putString(key, value.toString())
                    }
                }
            }
            analytics.logEvent(eventName, bundle)
            
            // Debug logging
            if (BuildConfig.DEBUG) {
                logD("FirebaseHelper", "Event logged: $eventName with ${parameters.size} parameters")
            }
        } else {
            analytics.logEvent(eventName, null)
            if (BuildConfig.DEBUG) {
                logD("FirebaseHelper", "Event logged: $eventName (no parameters)")
            }
        }
    }
    
    actual fun setUserProperty(name: String, value: String) {
        if (!isInitialized) return
        Firebase.analytics.setUserProperty(name, value)
    }
    
    actual fun setAnalyticsUserId(userId: String) {
        if (!isInitialized) return
        Firebase.analytics.setUserId(userId)
    }
    
    actual fun setAnalyticsCollectionEnabled(enabled: Boolean) {
        if (!isInitialized) return
        Firebase.analytics.setAnalyticsCollectionEnabled(enabled)
    }
}

actual fun createFirebaseHelper(): FirebaseHelper {
    return FirebaseHelper()
}

