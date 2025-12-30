package com.shoaib.notes_app_kmp.data.local

import androidx.room.Room
import com.shoaib.notes_app_kmp.PlatformContext
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

actual class SystemDatabaseFactory actual constructor(
    private val platformContext: PlatformContext
) {
    actual fun createSystemDatabase(): SystemDatabase {
        val context = platformContext.context
        val encryptionKey = getDatabaseEncryptionKey()
        val factory = SupportOpenHelperFactory(encryptionKey)

        return Room.databaseBuilder(
            context,
            SystemDatabase::class.java,
            "users.db" // System database name
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()

    }
}