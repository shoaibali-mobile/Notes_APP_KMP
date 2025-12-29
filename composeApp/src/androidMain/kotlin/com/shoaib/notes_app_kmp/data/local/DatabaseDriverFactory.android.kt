package com.shoaib.notes_app_kmp.data.local

import androidx.room.Room
import com.shoaib.notes_app_kmp.PlatformContext
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

actual class DatabaseDriverFactory actual constructor(
    private val platformContext: PlatformContext
) {
    actual fun createDriver(): NotesDatabase {
        val context = platformContext.context

        val encryptionKey = getDatabaseEncryptionKey()

        val factory = SupportOpenHelperFactory(encryptionKey)


        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "notes_database"
        )
            .openHelperFactory(factory)
            .build()
    }
}