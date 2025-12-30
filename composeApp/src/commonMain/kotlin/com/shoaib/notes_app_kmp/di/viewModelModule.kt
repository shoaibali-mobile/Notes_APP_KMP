package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.presentation.viewmodel.AuthViewModel
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module


val viewModelModule = module {
    factoryOf(::AuthViewModel)

    //NotesViewModel will be created dynamically with userId
    factory<NotesViewModel> { (userId: Int) ->
        val repository: NotesRepository = get { parametersOf(userId) }
        val addNoteUseCase = AddNoteUseCase(repository)
        NotesViewModel(repository, addNoteUseCase)
    }
}