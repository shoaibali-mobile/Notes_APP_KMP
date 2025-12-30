package com.shoaib.notes_app_kmp.data.local

import com.shoaib.notes_app_kmp.PlatformContext

expect class UserNotesDatabaseFactory(
    platformContext: PlatformContext
) {
    fun createDriver(userId:Int): NotesDatabase
    fun closeCurrentDatabase()
}