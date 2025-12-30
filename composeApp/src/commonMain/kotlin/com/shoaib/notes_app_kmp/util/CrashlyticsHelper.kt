package com.shoaib.notes_app_kmp.util

/**
 * Helper object for Firebase Crashlytics operations.
 * Uses expect/actual pattern to support both Android and iOS platforms.
 * 
 * Android: Uses google-services.json for configuration
 * iOS: Uses GoogleService-Info.plist for configuration
 */
object CrashlyticsHelper {
    
    private val firebaseHelper: FirebaseHelper = createFirebaseHelper()
    
    /**
     * Initialize Crashlytics.
     * Should be called once during app startup.
     * Android: Called in MainActivity.onCreate()
     * iOS: Called in iOSApp.swift or AppDelegate
     */
    fun initialize() {
        firebaseHelper.initialize()
    }
    
    /**
     * Log a message to Crashlytics.
     * Useful for debugging and tracking user actions.
     */
    fun log(message: String) {
        firebaseHelper.logCrashlytics(message)
    }
    
    /**
     * Log an exception to Crashlytics without crashing the app.
     * Useful for non-fatal errors.
     */
    fun recordException(throwable: Throwable) {
        firebaseHelper.recordException(throwable)
    }
    
    /**
     * Set a custom key-value pair for crash reports.
     * Useful for adding context like user ID, app version, etc.
     */
    fun setCustomKey(key: String, value: String) {
        firebaseHelper.setCustomKey(key, value)
    }
    
    /**
     * Set a custom key-value pair (Long) for crash reports.
     */
    fun setCustomKey(key: String, value: Long) {
        firebaseHelper.setCustomKey(key, value)
    }
    
    /**
     * Set user identifier for crash reports.
     * Useful for tracking which user experienced the crash.
     */
    fun setUserId(userId: String) {
        firebaseHelper.setUserId(userId)
    }
    
    /**
     * Enable or disable Crashlytics collection.
     * Useful for privacy settings or debug builds.
     */
    fun setCollectionEnabled(enabled: Boolean) {
        firebaseHelper.setCollectionEnabled(enabled)
    }
    
    /**
     * Check if Firebase is initialized
     */
    fun isInitialized(): Boolean {
        return firebaseHelper.isInitialized()
    }
}



