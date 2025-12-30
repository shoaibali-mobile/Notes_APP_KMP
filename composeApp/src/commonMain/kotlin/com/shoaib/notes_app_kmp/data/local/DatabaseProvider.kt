package com.shoaib.notes_app_kmp.data.local

object DatabaseProvider {
    private var database: NotesDatabase? = null

    fun get(factory: DatabaseDriverFactory): NotesDatabase {
        return database ?: factory.createDriver().also { database = it }
    }
}


