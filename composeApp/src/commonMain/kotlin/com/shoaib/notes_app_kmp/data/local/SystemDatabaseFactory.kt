package com.shoaib.notes_app_kmp.data.local

import com.shoaib.notes_app_kmp.PlatformContext

expect class SystemDatabaseFactory(
    platformContext: PlatformContext
){
    fun createSystemDatabase(): SystemDatabase
}