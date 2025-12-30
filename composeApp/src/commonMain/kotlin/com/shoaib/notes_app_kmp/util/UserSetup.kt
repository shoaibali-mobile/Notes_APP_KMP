package com.shoaib.notes_app_kmp.util

import com.shoaib.notes_app_kmp.getPlatform

/**
 * Sets up user information for Firebase Analytics and Crashlytics.
 * This should be called after user login/signup to set dynamic user IDs.
 */
object UserSetup {
    
    private const val TAG = "UserSetup"
    private var currentUserId: String? = null
    private var currentUserName: String? = null
    
    /**
     * Initialize Firebase without setting a default user.
     * User ID will be set dynamically after login/signup.
     */
    fun initializeFirebase() {
        logD(TAG, "Firebase initialized (no default user set)")
        logD(TAG, "Platform: ${getPlatform().name}")
        
        // Set device platform property (not user-specific)
        AnalyticsHelper.setUserProperty("device_platform", getPlatform().name)
        CrashlyticsHelper.setCustomKey("device_platform", getPlatform().name)
        
        logD(TAG, "✓ Device platform set for Analytics and Crashlytics")
    }
    
    /**
     * Update user information with dynamic user ID.
     * Should be called after successful login or signup.
     * 
     * @param userId The user's ID (from database)
     * @param userName The user's username/email
     */
    fun updateUser(userId: Int, userName: String) {
        val userIdString = userId.toString()
        
        // Skip if same user is already set
        if (currentUserId == userIdString && currentUserName == userName) {
            logD(TAG, "User already set: $userName (ID: $userIdString)")
            return
        }
        
        logD(TAG, "Setting Firebase user: $userName (ID: $userIdString)")
        logD(TAG, "Platform: ${getPlatform().name}")
        
        // Set user ID for both Analytics and Crashlytics
        AnalyticsHelper.setUserId(userIdString)
        CrashlyticsHelper.setUserId(userIdString)
        logD(TAG, "✓ User ID set for Analytics and Crashlytics: $userIdString")
        
        // Set user properties for Analytics
        AnalyticsHelper.setUserProperty("user_name", userName)
        AnalyticsHelper.setUserProperty("device_platform", getPlatform().name)
        AnalyticsHelper.setUserProperty("user_type", "authenticated")
        logD(TAG, "✓ User properties set for Analytics")
        
        // Set custom keys for Crashlytics
        CrashlyticsHelper.setCustomKey("user_id", userIdString)
        CrashlyticsHelper.setCustomKey("user_name", userName)
        CrashlyticsHelper.setCustomKey("device_platform", getPlatform().name)
        CrashlyticsHelper.setCustomKey("user_type", "authenticated")
        logD(TAG, "✓ Custom keys set for Crashlytics")
        
        // Log user login event
        CrashlyticsHelper.log("User authenticated: $userName (ID: $userIdString)")
        AnalyticsHelper.logEvent("user_authenticated", mapOf(
            "user_id" to userIdString,
            "user_name" to userName,
            "device_platform" to getPlatform().name
        ))
        logD(TAG, "✓ User authenticated event logged")
        
        // Update current user tracking
        currentUserId = userIdString
        currentUserName = userName
        
        logD(TAG, "✓ User setup completed successfully for: $userName (ID: $userIdString)")
    }
    
    /**
     * Clear user information (call on logout)
     */
    fun clearUser() {
        logD(TAG, "Clearing user information...")
        
        // Clear user ID (set to null/empty)
        AnalyticsHelper.setUserId("")
        CrashlyticsHelper.setUserId("")
        
        // Clear user properties
        AnalyticsHelper.setUserProperty("user_name", "")
        CrashlyticsHelper.setCustomKey("user_id", "")
        CrashlyticsHelper.setCustomKey("user_name", "")
        
        currentUserId = null
        currentUserName = null
        
        logD(TAG, "✓ User information cleared")
    }
    
    /**
     * Get current user ID (for logging in events)
     */
    fun getCurrentUserId(): String? = currentUserId
}

