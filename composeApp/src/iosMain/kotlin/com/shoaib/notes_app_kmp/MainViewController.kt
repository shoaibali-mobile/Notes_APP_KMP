package com.shoaib.notes_app_kmp



import androidx.compose.ui.window.ComposeUIViewController
import com.shoaib.notes_app_kmp.di.initKoin


fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}