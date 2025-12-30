package com.shoaib.notes_app_kmp.data.local

import android.content.Context
import androidx.room.Room
import com.shoaib.notes_app_kmp.PlatformContext
import com.shoaib.notes_app_kmp.util.logD
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.io.File

/**
 * Android implementation of UserNotesDatabaseFactory.
 * Creates user-specific notes databases (notes_user_ID.db) with SQLCipher encryption.
 * 
 * Each user gets their own isolated database file for privacy and security.
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
        val context = platformContext.context

        // Close previous database if switching users
        if (currentNotesDb != null) {
            logD("UserNotesDatabaseFactory", "Closing previous database connection")
            currentNotesDb?.close()
            currentNotesDb = null
        }

        val dbName = "notes_user_$userId.db"
        logD("UserNotesDatabaseFactory", "Creating/opening database: $dbName")

        // Clean up any corrupted database files for this user
        cleanupCorruptedDatabase(context, dbName)

        // Get encryption key and create SQLCipher factory
        val encryptionKey = getDatabaseEncryptionKey()
        val factory = SupportOpenHelperFactory(encryptionKey)

        // Create Room database with SQLCipher encryption
        val instance = Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            dbName
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration(false) // Recreate database if schema changes
            .build()

        currentNotesDb = instance
        logD("UserNotesDatabaseFactory", "✅ Database created successfully: $dbName")
        return instance
    }

    /**
     * Closes the current database connection.
     * Call this when logging out or switching users.
     */
    actual fun closeCurrentDatabase() {
        if (currentNotesDb != null) {
            logD("UserNotesDatabaseFactory", "Closing current database connection")
            currentNotesDb?.close()
            currentNotesDb = null
        }
    }

    /**
     * Cleans up corrupted or invalid database files for a specific user.
     * This handles the case where an unencrypted database exists but we're trying to open it with SQLCipher.
     */
    private fun cleanupCorruptedDatabase(context: Context, dbName: String) {
        val dbPath = context.getDatabasePath(dbName)
        val dbDir = dbPath.parentFile

        // List of database-related files to check
        val dbFiles = listOf(
            File(dbDir, dbName),
            File(dbDir, "$dbName-wal"),  // Write-Ahead Log
            File(dbDir, "$dbName-shm"),  // Shared Memory
            File(dbDir, "$dbName-journal") // Journal file
        )

        dbFiles.forEach { file ->
            if (file.exists()) {
                try {
                    // Check if it's an unencrypted SQLite database (which SQLCipher can't open)
                    if (isUnencryptedSQLiteDatabase(file)) {
                        logD("UserNotesDatabaseFactory", "🗑️ Found unencrypted SQLite database - deleting: ${file.name}")
                        file.delete()
                    } else if (!isValidSQLCipherDatabase(file)) {
                        // If we can't verify it's valid, delete it to be safe
                        // Room will recreate it with fallbackToDestructiveMigration
                        logD("UserNotesDatabaseFactory", "🗑️ Deleting unverifiable database file: ${file.name}")
                        file.delete()
                    }
                } catch (e: Exception) {
                    logD("UserNotesDatabaseFactory", "⚠️ Error checking database file ${file.name}: ${e.message}")
                    // If we can't verify, delete it to be safe
                    try {
                        file.delete()
                        logD("UserNotesDatabaseFactory", "🗑️ Deleted unverifiable database file: ${file.name}")
                    } catch (deleteException: Exception) {
                        logD("UserNotesDatabaseFactory", "❌ Failed to delete ${file.name}: ${deleteException.message}")
                    }
                }
            }
        }
    }

    /**
     * Checks if a file is an unencrypted SQLite database.
     * Unencrypted SQLite databases start with "SQLite format 3" (16 bytes).
     * These need to be deleted because SQLCipher can't open them.
     */
    private fun isUnencryptedSQLiteDatabase(file: File): Boolean {
        if (!file.exists() || file.length() < 16) {
            return false
        }

        return try {
            file.inputStream().use { input ->
                val header = ByteArray(16)
                val bytesRead = input.read(header)
                
                if (bytesRead < 16) {
                    return false
                }
                
                // Check if it starts with "SQLite format 3" (unencrypted SQLite header)
                val sqliteHeader = "SQLite format 3".toByteArray()
                header.sliceArray(0 until sqliteHeader.size).contentEquals(sqliteHeader)
            }
        } catch (e: Exception) {
            logD("UserNotesDatabaseFactory", "⚠️ Error reading database file header: ${e.message}")
            false
        }
    }

    /**
     * Checks if a file might be a valid SQLCipher database.
     * SQLCipher databases have an encrypted header, so we can't easily verify without the key.
     * We'll assume it's valid if it exists and has content (Room will validate it).
     */
    private fun isValidSQLCipherDatabase(file: File): Boolean {
        // For SQLCipher databases, we can't verify without the encryption key
        // So we'll be conservative - if it exists and has content, assume it might be valid
        // Room will handle the actual validation when trying to open it
        return file.exists() && file.length() >= 16 && !isUnencryptedSQLiteDatabase(file)
    }
}