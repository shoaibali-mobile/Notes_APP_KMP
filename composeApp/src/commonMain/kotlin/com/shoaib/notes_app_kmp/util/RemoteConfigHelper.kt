package com.shoaib.notes_app_kmp.util

import kotlinx.coroutines.flow.StateFlow

/**
 * Expect class for Firebase Remote Config operations.
 * Platform-specific implementations handle the actual Remote Config SDK calls.
 */
expect class RemoteConfigHelper {
    /**
     * Initialize Remote Config with default values.
     * Should be called once during app startup.
     */
    fun initialize()

    /**
     * Fetch remote config values from Firebase.
     * @param onComplete Callback with success/failure status
     */
    suspend fun fetch(onComplete: (Boolean) -> Unit = {})

    /**
     * Activate fetched config values.
     * @param onComplete Callback with success/failure status
     */
    suspend fun activate(onComplete: (Boolean) -> Unit = {})

    /**
     * Fetch and activate config values in one call.
     * @param onComplete Callback with success/failure status
     */
    suspend fun fetchAndActivate(onComplete: (Boolean) -> Unit = {})

    /**
     * Get a string value from Remote Config.
     * @param key The config key
     * @param defaultValue Default value if key not found
     * @return The config value as String
     */
    fun getString(key: String, defaultValue: String = ""): String

    /**
     * Get a boolean value from Remote Config.
     * @param key The config key
     * @param defaultValue Default value if key not found
     * @return The config value as Boolean
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    /**
     * Get a long value from Remote Config.
     * @param key The config key
     * @param defaultValue Default value if key not found
     * @return The config value as Long
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long

    /**
     * Get a double value from Remote Config.
     * @param key The config key
     * @param defaultValue Default value if key not found
     * @return The config value as Double
     */
    fun getDouble(key: String, defaultValue: Double = 0.0): Double
}

expect fun createRemoteConfigHelper(): RemoteConfigHelper

