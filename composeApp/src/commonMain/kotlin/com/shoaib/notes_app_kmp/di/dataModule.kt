package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.data.local.UserNotesDatabaseFactory
import com.shoaib.notes_app_kmp.data.local.DatabaseProvider
import com.shoaib.notes_app_kmp.data.local.NoteDao
import com.shoaib.notes_app_kmp.data.local.NotesDatabase
import com.shoaib.notes_app_kmp.data.local.NotesLocalDataSource
import com.shoaib.notes_app_kmp.data.local.SystemDatabase
import com.shoaib.notes_app_kmp.data.local.SystemDatabaseFactory
import com.shoaib.notes_app_kmp.data.local.SystemDatabaseProvider
import com.shoaib.notes_app_kmp.data.local.UserDao
import com.shoaib.notes_app_kmp.data.repository.AuthRepository
import com.shoaib.notes_app_kmp.data.repository.AuthRepositoryImpl
import com.shoaib.notes_app_kmp.data.repository.NotesRepositoryImpl
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val dataModule = module {

    single<SystemDatabase>{
        SystemDatabaseProvider.get(get<SystemDatabaseFactory>())
    }

    single<UserDao> {
        get<SystemDatabase>().userDao()
    }

    // Auth Repository
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }


    // User Notes Database (created dynamically per user)
    // Note: This will be created after login with userId
    // For now, we'll use factory pattern
    factory<NotesDatabase> { (userId: Int) ->
        DatabaseProvider.get(get<UserNotesDatabaseFactory>(), userId)
    }

    factory<NoteDao> { (userId: Int) ->
        get<NotesDatabase> { parametersOf(userId) }.noteDao()
    }


    factory<NotesLocalDataSource> { (userId: Int) ->
        NotesLocalDataSource(get<NoteDao> { parametersOf(userId) })
    }

    factory<NotesRepository> { (userId: Int) ->
        NotesRepositoryImpl(get<NotesLocalDataSource> { parametersOf(userId) })
    }
}