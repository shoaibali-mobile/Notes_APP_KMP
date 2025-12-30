package com.shoaib.notes_app_kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import com.shoaib.notes_app_kmp.util.CrashlyticsHelper
import com.shoaib.notes_app_kmp.util.UserSetup
import com.shoaib.notes_app_kmp.di.initKoin
import com.shoaib.notes_app_kmp.util.AppLogger

/**
 * iOS Main View Controller.
 *
 * This is the entry point for iOS app.
 *
 * IMPORTANT: SQLCipher native library loading
 * - SQLCipher requires native library to be loaded before database operations
 * - After CocoaPods setup, uncomment the dlopen line below
 * - Or use static linking if SQLCipher is statically linked
 */
fun MainViewController() = ComposeUIViewController {
    // CRITICAL: Load SQLCipher native library BEFORE initializing database
    // This must be called before any database operations
    // Uncomment after SQLCipher CocoaPods is set up:
    /*
    try {
        // Load SQLCipher native library
        // The library is included via CocoaPods
        platform.darwin.dlopen("libsqlcipher.dylib", platform.darwin.RTLD_LAZY)
        AppLogger.d("SQLCipher-iOS", "✅ SQLCipher native library loaded successfully")
    } catch (e: Exception) {
        AppLogger.d("SQLCipher-iOS", "⚠️ Warning: Could not load SQLCipher library: ${e.message}")
        // Continue anyway - might work if library is statically linked
    }
    */

    AppLogger.d("iOS-App", "🚀 Initializing iOS app...")

    // Initialize Firebase services (uses expect/actual pattern)
    CrashlyticsHelper.initialize()
    AnalyticsHelper.initialize()

    // Initialize Firebase (without default user - user ID will be set after login)
    UserSetup.initializeFirebase()

    initKoin()

    // Log app launch event (no user ID until login)
    AnalyticsHelper.logEvent("app_launched", mapOf(
        "build_type" to "release" // iOS doesn't have BuildConfig, use "release" as default
    ))

    App()
}