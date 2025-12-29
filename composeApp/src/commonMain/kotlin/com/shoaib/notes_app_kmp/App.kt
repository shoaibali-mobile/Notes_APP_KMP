package com.shoaib.notes_app_kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shoaib.notes_app_kmp.data.local.DatabaseDriverFactory
import com.shoaib.notes_app_kmp.data.local.NotesLocalDataSource
import com.shoaib.notes_app_kmp.data.repository.NotesRepositoryImpl
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NoteEditorScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NotesListScreen
import com.shoaib.notes_app_kmp.presentation.ui.theme.NotesAppTheme
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    NotesAppTheme {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    val navController = rememberNavController()
    
    val viewModelFactory = remember {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val platformContext = getPlatformContext()
                val databaseFactory = DatabaseDriverFactory(platformContext)
                val database = databaseFactory.createDriver()
                val noteDao = database.noteDao()
                val localDataSource = NotesLocalDataSource(noteDao)
                val repository = NotesRepositoryImpl(localDataSource)
                val addNoteUseCase = AddNoteUseCase(repository)
                
                return NotesViewModel(repository, addNoteUseCase) as T
            }
        }
    }
    
    val viewModel: NotesViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Screen.NotesList.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.NotesList.route) {
            // Track screen view
            LaunchedEffect(Unit) {
                AnalyticsHelper.logEvent("screen_view", mapOf(
                    "screen_name" to "notes_list"
                ))
            }
            
            NotesListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.NoteEditor.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
            val notes by viewModel.notes.collectAsState()
            val note = notes.find { it.id == noteId }
            val noteIdForEditor = if (noteId == 0L) null else noteId
            
            // Track screen view
            LaunchedEffect(noteId) {
                AnalyticsHelper.logEvent("screen_view", mapOf(
                    "screen_name" to "note_editor",
                    "is_new_note" to (noteId == 0L),
                    "note_id" to noteId
                ))
            }
            
            NoteEditorScreen(
                noteId = noteIdForEditor,
                initialTitle = note?.title ?: "",
                initialContent = note?.description ?: "",
                onBackClick = { navController.popBackStack() },
                onSaveClick = { title, content ->
                    viewModel.addOrUpdateNote(title, content, noteIdForEditor)
                }
            )
        }
    }
}
