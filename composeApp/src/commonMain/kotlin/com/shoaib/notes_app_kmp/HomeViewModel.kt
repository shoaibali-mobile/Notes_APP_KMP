package com.shoaib.notes_app_kmp

import androidx.lifecycle.ViewModel
import com.shoaib.notes_app_kmp.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class HomeViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(
        listOf(
            Note(
                title = "Welcome to Notes App",
                description = "This is your first note. You can create, edit, and delete notes to keep track of your thoughts and ideas."
            ),
            Note(
                title = "Kotlin Multiplatform",
                description = "This app is built with Kotlin Multiplatform (KMP) and Compose Multiplatform, allowing it to run on both Android and iOS with shared code."
            ),
            Note(
                title = "MVVM Architecture",
                description = "The app follows MVVM architecture pattern with ViewModels managing the business logic and state."
            ),
            Note(
                title = "Material Design 3",
                description = "The UI uses Material Design 3 components for a modern and consistent look across platforms."
            ),
            Note(
                title = "Custom Fonts",
                description = "The app uses Nunito font family with various weights and styles for beautiful typography."
            ),
            Note(
                title = "Dependency Injection",
                description = "You can use Koin or Kodein for dependency injection in KMP projects to manage dependencies cleanly."
            ),
            Note(
                title = "State Management",
                description = "State is managed using StateFlow and MutableStateFlow from Kotlin Coroutines for reactive UI updates."
            ),
            Note(
                title = "Compose Multiplatform",
                description = "Compose Multiplatform allows you to write UI once and run it on Android, iOS, Desktop, and Web."
            )
        )
    )
    val notes: StateFlow<List<Note>> = _notes

    fun addNotes(note: Note) {
        _notes.update { it + note }
    }
}