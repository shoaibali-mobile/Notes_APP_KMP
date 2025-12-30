package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.appContext
import org.koin.core.KoinApplication
import org.koin.android.ext.koin.androidContext


actual fun KoinApplication.setupPlatformContext() {
    androidContext(appContext)
}