package com.shoaib.notes_app_kmp.domain.usecase

import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val notesRepository: NotesRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return notesRepository.getAllNotes()
    }

}