package com.shoaib.notes_app_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shoaib.notes_app_kmp.di.initKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        // CRITICAL: Load SQLCipher native library BEFORE initializing database
        // This must be called before any database operations
        // SQLiteDatabase.loadLibs() loads the native SQLCipher library
        System.loadLibrary("sqlcipher")

        initAppContext(applicationContext)
        initKoin()


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