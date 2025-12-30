package com.shoaib.notes_app_kmp.data.local

object SystemDatabaseProvider {
    private var database: SystemDatabase? = null

    fun get(factory: SystemDatabaseFactory): SystemDatabase {
        return database ?: factory.createSystemDatabase().also { database = it }
    }
}