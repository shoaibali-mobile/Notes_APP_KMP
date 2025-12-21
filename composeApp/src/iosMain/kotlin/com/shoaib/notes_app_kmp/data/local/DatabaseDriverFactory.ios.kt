package com.shoaib.notes_app_kmp.data.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.shoaib.notes_app_kmp.PlatformContext
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class DatabaseDriverFactory actual constructor(
    platformContext: PlatformContext
) {
    actual fun createDriver(): NotesDatabase {
        val dbPath = getDatabasePath()
        return Room.databaseBuilder<NotesDatabase>(
            name = dbPath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    private fun getDatabasePath(): String {
        val fileManager = NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        )
        val documentsDirectory = urls.firstOrNull() as? NSURL
            ?: throw IllegalStateException("Documents directory not found")

        val dbPath = documentsDirectory.path
            ?: throw IllegalStateException("Database path not found")

        return "$dbPath/notes_database.db"
    }
}