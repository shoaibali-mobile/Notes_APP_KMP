package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val viewModelModule = module {
    factoryOf(::NotesViewModel)
}