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
 * iOS implementation of UserNotesDatabaseFactory with SQLCipher encryption.
 * 
 * NOTE: SQLCipher for iOS requires:
 * 1. CocoaPods dependency: pod 'SQLCipher', '~> 4.5.4'
 * 2. Native library loaded before database operations (in MainViewController)
 * 
 * HOW IT WORKS:
 * 1. Gets encryption key from Keychain (secure storage)
 * 2. Uses SQLCipher native library to encrypt the database
 * 3. Database file on disk is encrypted with AES-256
 * 
 * CURRENT STATUS:
 * - Uses BundledSQLiteDriver (regular SQLite) for now
 * - TODO: Integrate SQLCipher driver when CocoaPods is set up
 * - Encryption key is retrieved and ready to use
 */
actual class UserNotesDatabaseFactory actual constructor(
    private val platformContext: PlatformContext
) {
    private var currentNotesDb: NotesDatabase? = null

    /**
     * Creates or retrieves a user-specific notes database.
     * 
     * @param userId The ID of the user whose database to create/open
     * @return NotesDatabase instance for the specified user
     */
    actual fun createDriver(userId: Int): NotesDatabase {
        val tag = "UserNotesDatabaseFactory-iOS"
        
        // Close previous database if switching users
        if (currentNotesDb != null) {
            AppLogger.d(tag, "Closing previous database connection")
            currentNotesDb?.close()
            currentNotesDb = null
        }

        val dbName = "notes_user_$userId.db"
        AppLogger.d(tag, "Creating/opening database: $dbName")
        
        val dbPath = getDatabasePath(dbName)
        
        // Get encryption key from Keychain
        val encryptionKey = getDatabaseEncryptionKey()
        
        AppLogger.d(tag, "🔑 Retrieved encryption key from Keychain (${encryptionKey.size} bytes)")
        
        // Delete old unencrypted database if it exists
        // This is needed when migrating from unencrypted to encrypted database
        val fileManager = NSFileManager.defaultManager
        if (fileManager.fileExistsAtPath(dbPath)) {
            try {
                fileManager.removeItemAtPath(dbPath, null)
                AppLogger.d(tag, "🗑️ Deleted old unencrypted database")
            } catch (e: Exception) {
                AppLogger.d(tag, "⚠️ Could not delete old database: ${e.message}")
            }
        }
        
        // Also delete WAL and SHM files if they exist
        val walPath = "$dbPath-wal"
        val shmPath = "$dbPath-shm"
        if (fileManager.fileExistsAtPath(walPath)) {
            try {
                fileManager.removeItemAtPath(walPath, null)
                AppLogger.d(tag, "🗑️ Deleted old WAL file")
            } catch (e: Exception) {
                // Ignore
            }
        }
        if (fileManager.fileExistsAtPath(shmPath)) {
            try {
                fileManager.removeItemAtPath(shmPath, null)
                AppLogger.d(tag, "🗑️ Deleted old SHM file")
            } catch (e: Exception) {
                // Ignore
            }
        }
        
        // TODO: Replace BundledSQLiteDriver with SQLCipher driver
        // For now, using regular SQLite driver
        // When SQLCipher CocoaPods is set up, use SQLCipher driver here
        AppLogger.d(tag, "⚠️ Using BundledSQLiteDriver - SQLCipher integration pending")
        AppLogger.d(tag, "💡 To enable encryption: Set up SQLCipher via CocoaPods and use SQLCipher driver")
        
        // Create Room database
        // Note: Currently using regular SQLite driver
        // After SQLCipher setup, replace with SQLCipher driver
        val database = Room.databaseBuilder<NotesDatabase>(
            name = dbPath
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration()  // Allow Room to recreate database if schema changes
            .build()
        
        AppLogger.d(tag, "✅ Database created successfully: $dbName")
        
        currentNotesDb = database
        return database
    }

    /**
     * Closes the current database connection.
     * Call this when logging out or switching users.
     */
    actual fun closeCurrentDatabase() {
        if (currentNotesDb != null) {
            AppLogger.d("UserNotesDatabaseFactory-iOS", "Closing current database connection")
            currentNotesDb?.close()
            currentNotesDb = null
        }
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