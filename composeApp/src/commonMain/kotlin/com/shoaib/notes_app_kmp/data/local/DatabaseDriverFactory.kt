package com.shoaib.notes_app_kmp.data.local

import com.shoaib.notes_app_kmp.PlatformContext

expect class DatabaseDriverFactory(
    platformContext: PlatformContext
) {
    fun createDriver(): NotesDatabase
}