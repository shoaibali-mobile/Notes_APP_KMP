package com.shoaib.notes_app_kmp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.shoaib.notes_app_kmp.domain.model.Note
i
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class NotesViewModel(
) : ViewModel() {
    
    private val _notes = MutableStateFlow<List<Note>>(
        listOf(
            Note(
                id = 1,
                title = "Welcome to Notes App",
                description = "This is your first note. Start creating amazing notes!"
            ),
            Note(
                id = 2,
                title = "Shopping List",
                description = "Milk, Eggs, Bread, Butter"
            ),
            Note(
                id = 3,
                title = "Meeting Notes",
                description = "Discuss project timeline and deliverables with the team."
            ),
            Note(
                id = 4,
                title = "Ideas",
                description = "Brainstorm new features for the app. Think about user experience improvements."
            )
        )
    )
    val notes: StateFlow<List<Note>> = _notes




    

}

