package com.shoaib.notes_app_kmp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import com.shoaib.notes_app_kmp.util.CrashlyticsHelper
import kotlinx.coroutines.flow.SharingStarted
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class NotesViewModel(
    private val notesRepository: NotesRepository,
    private val addNoteUseCase: AddNoteUseCase
    private val getNotesUseCase: GetNotesUseCase,
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
            try {
                val note = Note(
                    id = noteId ?: 0L,
                    title = title,
                    description = content
                )

                val isNewNote = noteId == null || noteId == 0L
                val noteLength = content.length

                addNoteUseCase(note)

                // Log analytics event
                AnalyticsHelper.logEvent(
                    if (isNewNote) "note_created" else "note_updated",
                    mapOf(
                        "note_id" to (note.id),
                        "title_length" to title.length,
                        "content_length" to noteLength,
                        "has_title" to title.isNotBlank(),
                        "has_content" to content.isNotBlank()
                    )
                )

                // Log to Crashlytics for debugging
                CrashlyticsHelper.log("Note ${if (isNewNote) "created" else "updated"}: ID=${note.id}, Title length=${title.length}, Content length=$noteLength")

            } catch (e: Exception) {
                // Log exception to Crashlytics
                CrashlyticsHelper.recordException(e)

                // Log error event to Analytics
                AnalyticsHelper.logEvent("note_save_error", mapOf(
                    "error_message" to (e.message ?: "Unknown error"),
                    "is_new_note" to (noteId == null || noteId == 0L)
                ))

                // Re-throw to let UI handle it
                throw e
            }
        }
    }

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

