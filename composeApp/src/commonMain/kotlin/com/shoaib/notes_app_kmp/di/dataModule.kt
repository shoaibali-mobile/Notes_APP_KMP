package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.data.local.DatabaseDriverFactory
import com.shoaib.notes_app_kmp.data.local.DatabaseProvider
import com.shoaib.notes_app_kmp.data.local.NoteDao
import com.shoaib.notes_app_kmp.data.local.NotesDatabase
import com.shoaib.notes_app_kmp.data.local.NotesLocalDataSource
import com.shoaib.notes_app_kmp.data.repository.NotesRepositoryImpl
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import org.koin.dsl.module

val dataModule = module {
    single<NotesDatabase> {
        DatabaseProvider.get(get<DatabaseDriverFactory>())
    }

    single<NoteDao> {
        get<NotesDatabase>().noteDao()
    }

    single {
        NotesLocalDataSource(get())
    }

    single<NotesRepository> {
        NotesRepositoryImpl(get())
    }
}