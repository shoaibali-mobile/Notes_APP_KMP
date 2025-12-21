package com.shoaib.notes_app_kmp.di

import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.domain.usecase.GetNotesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::AddNoteUseCase)
    factoryOf(::GetNotesUseCase)
}