package com.shoaib.notes_app_kmp.domain.usecase

import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository

class AddNoteUseCase(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(note: Note): Long {
        return notesRepository.addNote(note)
    }
}


