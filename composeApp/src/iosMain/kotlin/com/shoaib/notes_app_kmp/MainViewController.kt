package com.shoaib.notes_app_kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import com.shoaib.notes_app_kmp.util.CrashlyticsHelper
import com.shoaib.notes_app_kmp.util.UserSetup

fun MainViewController() = ComposeUIViewController {
    // Initialize Firebase services (uses expect/actual pattern)
    CrashlyticsHelper.initialize()
    AnalyticsHelper.initialize()
    
    // Setup default user information
    UserSetup.setupDefaultUser()
    
    // Log app launch event
    AnalyticsHelper.logEvent("app_launched")
    
    App()
}