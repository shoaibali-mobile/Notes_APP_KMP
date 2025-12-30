package com.shoaib.notes_app_kmp.data.local

/**
 * Provider for user-specific notes databases.
 * Creates and manages user-specific database instances.
 */
object DatabaseProvider {
    private var database: NotesDatabase? = null
    private var currentUserId: Int? = null

    /**
     * Gets or creates a user-specific notes database.
     * 
     * @param factory The factory to create the database
     * @param userId The ID of the user whose database to get/create
     * @return NotesDatabase instance for the specified user
     */
    fun get(factory: UserNotesDatabaseFactory, userId: Int): NotesDatabase {
        // If switching users, close the old database
        if (currentUserId != null && currentUserId != userId) {
            database?.close()
            database = null
        }
        
        // Create new database if needed
        return database ?: factory.createDriver(userId).also { 
            database = it
            currentUserId = userId
        }
    }

    /**
     * Clears the current database instance.
     * Call this when logging out.
     */
    fun clear() {
        database?.close()
        database = null
        currentUserId = null
    }
}



