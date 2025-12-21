package com.shoaib.notes_app_kmp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class NotesViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            getNotesUseCase().collect { notesList ->
                _notes.value = notesList
            }
        }
    }

    fun addNote(title: String, description: String) {
        if (title.isNotBlank() && description.isNotBlank()) {
            viewModelScope.launch {
                val note = Note(
                    title = title.trim(),
                    description = description.trim()
                )
                addNoteUseCase(note)
            }
        }
    }

}

