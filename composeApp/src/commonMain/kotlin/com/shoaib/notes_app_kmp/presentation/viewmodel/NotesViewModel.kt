package com.shoaib.notes_app_kmp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(
    private val notesRepository: NotesRepository,
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {
    
    val notes: StateFlow<List<Note>> = notesRepository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun addOrUpdateNote(title: String, content: String, noteId: Long? = null) {
        viewModelScope.launch {
            val note = Note(
                id = noteId ?: 0L,
                title = title,
                description = content
            )
            addNoteUseCase(note)
        }
    }




    

}

