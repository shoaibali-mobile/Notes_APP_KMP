package com.shoaib.notes_app_kmp.data.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.shoaib.notes_app_kmp.PlatformContext
import com.shoaib.notes_app_kmp.util.AppLogger
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * iOS implementation of SystemDatabaseFactory.
 * Creates the system database (users.db) for storing user credentials.
 */
actual class SystemDatabaseFactory actual constructor(
    private val platformContext: PlatformContext
) {
    actual fun createSystemDatabase(): SystemDatabase {
        val tag = "SystemDatabase-iOS"
        
        AppLogger.d(tag, "🔐 Initializing system database (iOS)...")
        
        val dbPath = getDatabasePath("users.db")
        
        // Get encryption key from Keychain
        val encryptionKey = getDatabaseEncryptionKey()
        
        AppLogger.d(tag, "🔑 Retrieved encryption key from Keychain (${encryptionKey.size} bytes)")
        
        // TODO: Replace BundledSQLiteDriver with SQLCipher driver
        // For now, using regular SQLite driver
        AppLogger.d(tag, "⚠️ Using BundledSQLiteDriver - SQLCipher integration pending")
        
        // Create Room database
        val database = Room.databaseBuilder<SystemDatabase>(
            name = dbPath
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration()
            .build()
        
        AppLogger.d(tag, "✅ System database initialized successfully")
        
        return database
    }

    private fun getDatabasePath(dbName: String): String {
        val fileManager = NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask
        )
        val documentsDirectory = urls.firstOrNull() as? NSURL
            ?: throw IllegalStateException("Documents directory not found")

        val dbPath = documentsDirectory.path
            ?: throw IllegalStateException("Database path not found")

        return "$dbPath/$dbName"
    }
}

