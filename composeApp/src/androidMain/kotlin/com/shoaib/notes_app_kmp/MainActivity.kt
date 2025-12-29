package com.shoaib.notes_app_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.shoaib.notes_app_kmp.BuildConfig
import com.shoaib.notes_app_kmp.initAppContext
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import com.shoaib.notes_app_kmp.util.CrashlyticsHelper
import com.shoaib.notes_app_kmp.util.UserSetup
import com.shoaib.notes_app_kmp.util.logD

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initAppContext(applicationContext)
        
        // Enable Firebase Analytics collection (enabled in all modes: debug and release)
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
        if (BuildConfig.DEBUG) {
            logD("MainActivity", "=== DEBUG MODE: Firebase Enabled ===")
            logD("MainActivity", "DebugView: Run 'adb shell setprop debug.firebase.analytics.app com.shoaib.notes_app_kmp'")
            logD("MainActivity", "Then check: Firebase Console > Analytics > DebugView")
        }
        
        // Initialize Firebase services (uses expect/actual pattern)
        // Firebase is enabled in ALL modes (debug and release)
        if (BuildConfig.DEBUG) {
            logD("MainActivity", "Initializing Firebase services...")
        }
        CrashlyticsHelper.initialize()
        AnalyticsHelper.initialize()
        if (BuildConfig.DEBUG) {
            logD("MainActivity", "✓ Firebase services initialized")
        }

        // Setup default user information
        UserSetup.setupDefaultUser()
        
        // Log app launch event
        AnalyticsHelper.logEvent("app_launched", mapOf(
            "build_type" to if (BuildConfig.DEBUG) "debug" else "release"
        ))
        if (BuildConfig.DEBUG) {
            logD("MainActivity", "✓ App launch event logged")
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}