package com.shoaib.notes_app_kmp.data.local

import androidx.room.Room
import com.shoaib.notes_app_kmp.PlatformContext

actual class DatabaseDriverFactory actual constructor(
    private val platformContext: PlatformContext
) {
    actual fun createDriver(): NotesDatabase {
        val context = platformContext.context
        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "notes_database"
        ).build()
    }
}