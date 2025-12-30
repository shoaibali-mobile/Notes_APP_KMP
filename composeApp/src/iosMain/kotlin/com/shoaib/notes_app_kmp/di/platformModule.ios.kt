package com.shoaib.notes_app_kmp.di


import com.shoaib.notes_app_kmp.PlatformContext
import com.shoaib.notes_app_kmp.data.local.DatabaseDriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single {
        DatabaseDriverFactory(PlatformContext())
    }
}