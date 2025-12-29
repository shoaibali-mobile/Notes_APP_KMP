package com.shoaib.notes_app_kmp.util

import com.shoaib.notes_app_kmp.getPlatform

/**
 * Sets up default user information for Firebase Analytics and Crashlytics.
 * This should be called after Firebase initialization.
 */
object UserSetup {
    
    private const val DEFAULT_USER_ID = "6t387"
    private const val DEFAULT_USER_NAME = "shoaib ali"
    private const val TAG = "UserSetup"
    
    /**
     * Configure default user and device information.
     * Sets user ID, user properties, and device information for both Analytics and Crashlytics.
     */
    fun setupDefaultUser() {
        // Debug logging
        logD(TAG, "Starting user setup...")
        logD(TAG, "User ID: $DEFAULT_USER_ID")
        logD(TAG, "User Name: $DEFAULT_USER_NAME")
        logD(TAG, "Platform: ${getPlatform().name}")
        
        // Set user ID for both Analytics and Crashlytics
        AnalyticsHelper.setUserId(DEFAULT_USER_ID)
        CrashlyticsHelper.setUserId(DEFAULT_USER_ID)
        logD(TAG, "✓ User ID set for Analytics and Crashlytics")
        
        // Set user properties for Analytics
        AnalyticsHelper.setUserProperty("user_name", DEFAULT_USER_NAME)
        AnalyticsHelper.setUserProperty("device_platform", getPlatform().name)
        AnalyticsHelper.setUserProperty("user_type", "default")
        logD(TAG, "✓ User properties set for Analytics")
        
        // Set custom keys for Crashlytics
        CrashlyticsHelper.setCustomKey("user_id", DEFAULT_USER_ID)
        CrashlyticsHelper.setCustomKey("user_name", DEFAULT_USER_NAME)
        CrashlyticsHelper.setCustomKey("device_platform", getPlatform().name)
        CrashlyticsHelper.setCustomKey("user_type", "default")
        logD(TAG, "✓ Custom keys set for Crashlytics")
        
        // Log setup completion
        CrashlyticsHelper.log("User setup completed: $DEFAULT_USER_NAME (ID: $DEFAULT_USER_ID)")
        AnalyticsHelper.logEvent("user_setup_completed", mapOf(
            "user_id" to DEFAULT_USER_ID,
            "user_name" to DEFAULT_USER_NAME,
            "device_platform" to getPlatform().name
        ))
        logD(TAG, "✓ User setup completed event logged")
        
        // Log test event to verify everything works
        AnalyticsHelper.logEvent("test_user_setup", mapOf(
            "user_id" to DEFAULT_USER_ID,
            "user_name" to DEFAULT_USER_NAME,
            "device_platform" to getPlatform().name,
            "test" to "true"
        ))
        logD(TAG, "✓ Test event 'test_user_setup' logged")
        
        logD(TAG, "User setup completed successfully!")
    }
    
    /**
     * Update user information (can be called when user logs in or changes)
     */
    fun updateUser(userId: String, userName: String) {
        AnalyticsHelper.setUserId(userId)
        CrashlyticsHelper.setUserId(userId)
        
        AnalyticsHelper.setUserProperty("user_name", userName)
        CrashlyticsHelper.setCustomKey("user_id", userId)
        CrashlyticsHelper.setCustomKey("user_name", userName)
        
        AnalyticsHelper.logEvent("user_updated", mapOf(
            "user_id" to userId,
            "user_name" to userName
        ))
    }
}

