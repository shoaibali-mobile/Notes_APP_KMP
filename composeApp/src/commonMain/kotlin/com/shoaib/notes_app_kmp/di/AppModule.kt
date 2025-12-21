package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.data.local.DatabaseDriverFactory
import com.shoaib.notes_app_kmp.data.local.DatabaseProvider
import com.shoaib.notes_app_kmp.data.local.NoteDao
import com.shoaib.notes_app_kmp.data.local.NotesDatabase
import com.shoaib.notes_app_kmp.data.local.NotesLocalDataSource
import com.shoaib.notes_app_kmp.data.repository.NotesRepositoryImpl
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.domain.usecase.GetNotesUseCase
import com.shoaib.notes_app_kmp.getPlatformContext
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import org.koin.core.module.dsl.factoryOf

import org.koin.dsl.module



val appModule = module{

    // Database Factory - Lazy creation (only when first accessed)
    single {
        DatabaseDriverFactory(getPlatformContext())
    }

    // Database - Lazy creation
    single<NotesDatabase> {
        DatabaseProvider.get(get<DatabaseDriverFactory>())
    }

    // DAO - Single instance (explicit singleton)
    single<NoteDao> {
        get<NotesDatabase>().noteDao()
    }

   // Data Source - Single instance
    single {
        NotesLocalDataSource(get())
    }

    // Repository - Single instance
    single<NotesRepository> {
        NotesRepositoryImpl(get())
    }

   // Use Case - Factory (new instance each time)
    factoryOf(::AddNoteUseCase)
    factoryOf(::GetNotesUseCase)



    // ViewModel - Factory (new instance per screen)
    factoryOf(::NotesViewModel)


}


