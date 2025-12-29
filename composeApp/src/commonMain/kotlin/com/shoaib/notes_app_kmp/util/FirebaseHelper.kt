package com.shoaib.notes_app_kmp.util

/**
 * Expect class for Firebase initialization, Crashlytics, and Analytics operations.
 * Platform-specific implementations handle the actual Firebase SDK calls.
 * 
 * Note: Functions inside an 'expect class' don't need the 'expect' keyword
 * because they're automatically part of the expect declaration.
 * Only standalone functions (like 'createFirebaseHelper()' below) need 'expect'.
 */
expect class FirebaseHelper {
    /**
     * Initialize Firebase with platform-specific configuration.
     * Android uses google-services.json, iOS uses GoogleService-Info.plist
     */
    fun initialize()
    
    /**
     * Check if Firebase is initialized
     */
    fun isInitialized(): Boolean
    
    // Crashlytics methods
    /**
     * Log a message to Crashlytics
     */
    fun logCrashlytics(message: String)
    
    /**
     * Log an exception to Crashlytics
     */
    fun recordException(throwable: Throwable)
    
    /**
     * Set a custom key-value pair (String) for crash reports
     */
    fun setCustomKey(key: String, value: String)
    
    /**
     * Set a custom key-value pair (Long) for crash reports
     */
    fun setCustomKey(key: String, value: Long)
    
    /**
     * Set user identifier for crash reports
     */
    fun setUserId(userId: String)
    
    /**
     * Enable or disable Crashlytics collection
     */
    fun setCollectionEnabled(enabled: Boolean)
    
    // Analytics methods
    /**
     * Log an analytics event with optional parameters
     * @param eventName The name of the event
     * @param parameters Optional map of parameters (key-value pairs)
     */
    fun logEvent(eventName: String, parameters: Map<String, Any>? = null)
    
    /**
     * Set a user property for analytics
     * @param name The name of the user property
     * @param value The value of the user property
     */
    fun setUserProperty(name: String, value: String)
    
    /**
     * Set user identifier for analytics
     * @param userId The user identifier
     */
    fun setAnalyticsUserId(userId: String)
    
    /**
     * Enable or disable Analytics collection
     * @param enabled Whether to enable analytics collection
     */
    fun setAnalyticsCollectionEnabled(enabled: Boolean)
}

expect fun createFirebaseHelper(): FirebaseHelper

