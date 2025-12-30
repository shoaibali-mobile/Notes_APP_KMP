package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.PlatformContext
import com.shoaib.notes_app_kmp.data.local.SystemDatabaseFactory
import com.shoaib.notes_app_kmp.data.local.UserNotesDatabaseFactory
import org.koin.dsl.module

actual val platformModule = module {
    single {
        SystemDatabaseFactory(PlatformContext())
    }
    
    single {
        UserNotesDatabaseFactory(PlatformContext())
    }
}