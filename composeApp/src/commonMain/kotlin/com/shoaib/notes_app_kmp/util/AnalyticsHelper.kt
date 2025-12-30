package com.shoaib.notes_app_kmp.util

/**
 * Helper object for Firebase Analytics operations.
 * Uses expect/actual pattern to support both Android and iOS platforms.
 * 
 * Android: Uses google-services.json for configuration
 * iOS: Uses GoogleService-Info.plist for configuration
 */
object AnalyticsHelper {
    
    private val firebaseHelper: FirebaseHelper = createFirebaseHelper()
    
    /**
     * Initialize Analytics (Firebase is initialized via FirebaseHelper.initialize()).
     * Should be called once during app startup.
     * Android: Called in MainActivity.onCreate()
     * iOS: Called in iOSApp.swift or AppDelegate
     */
    fun initialize() {
        firebaseHelper.initialize()
    }
    
    /**
     * Log an analytics event.
     * 
     * @param eventName The name of the event (e.g., "button_click", "screen_view")
     * @param parameters Optional map of parameters (e.g., mapOf("screen_name" to "Home", "item_id" to "123"))
     * 
     * Example:
     * ```
     * AnalyticsHelper.logEvent("note_created", mapOf("note_id" to noteId, "category" to category))
     * ```
     */
    fun logEvent(eventName: String, parameters: Map<String, Any>? = null) {
        firebaseHelper.logEvent(eventName, parameters)
    }
    
    /**
     * Set a user property for analytics.
     * User properties are attributes that describe segments of your user base.
     * 
     * @param name The name of the user property
     * @param value The value of the user property
     * 
     * Example:
     * ```
     * AnalyticsHelper.setUserProperty("favorite_category", "work")
     * ```
     */
    fun setUserProperty(name: String, value: String) {
        firebaseHelper.setUserProperty(name, value)
    }
    
    /**
     * Set user identifier for analytics.
     * Useful for tracking user behavior across sessions.
     * 
     * @param userId The user identifier
     * 
     * Example:
     * ```
     * AnalyticsHelper.setUserId("user_123")
     * ```
     */
    fun setUserId(userId: String) {
        firebaseHelper.setAnalyticsUserId(userId)
    }
    
    /**
     * Enable or disable Analytics collection.
     * Useful for privacy settings or debug builds.
     * 
     * @param enabled Whether to enable analytics collection
     */
    fun setCollectionEnabled(enabled: Boolean) {
        firebaseHelper.setAnalyticsCollectionEnabled(enabled)
    }
    
    /**
     * Check if Firebase is initialized
     */
    fun isInitialized(): Boolean {
        return firebaseHelper.isInitialized()
    }
}



