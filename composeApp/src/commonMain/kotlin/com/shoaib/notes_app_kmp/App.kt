package com.shoaib.notes_app_kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import com.shoaib.notes_app_kmp.domain.usecase.AddNoteUseCase
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.screens.auth.LoginScreen
import com.shoaib.notes_app_kmp.presentation.screens.auth.SignupScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NoteEditorScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NotesListScreen
import com.shoaib.notes_app_kmp.presentation.ui.theme.NotesAppTheme
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

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

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Login.route) {
            // Track screen view
            LaunchedEffect(Unit) {
                AnalyticsHelper.logEvent("screen_view", mapOf(
                    "screen_name" to "login"
                ))
            }
            LoginScreen(navController = navController)
        }

        composable(Screen.Signup.route) {
            // Track screen view
            LaunchedEffect(Unit) {
                AnalyticsHelper.logEvent("screen_view", mapOf(
                    "screen_name" to "signup"
                ))
            }
            SignupScreen(navController = navController)
        }

        composable(
            route = Screen.NotesList.route,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") 
                ?: return@composable
            
            // Track screen view
            LaunchedEffect(userId) {
                AnalyticsHelper.logEvent("screen_view", mapOf(
                    "screen_name" to "notes_list",
                    "user_id" to userId
                ))
            }

            // Create ViewModel with userId
            val notesRepository: NotesRepository = koinInject { parametersOf(userId) }
            val addNoteUseCase = AddNoteUseCase(notesRepository)
            val viewModel = NotesViewModel(notesRepository, addNoteUseCase)

            NotesListScreen(
                navController = navController,
                viewModel = viewModel,
                userId = userId
            )
        }

        composable(
            route = Screen.NoteEditor.route,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                },
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") 
                ?: return@composable
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
            
            // Create ViewModel with userId
            val notesRepository: NotesRepository = koinInject { parametersOf(userId) }
            val addNoteUseCase = AddNoteUseCase(notesRepository)
            val viewModel = NotesViewModel(notesRepository, addNoteUseCase)
            
            val notes by viewModel.notes.collectAsState()
            val note = notes.find { it.id == noteId }
            val noteIdForEditor = if (noteId == 0L) null else noteId

            // Track screen view
            LaunchedEffect(noteId) {
                AnalyticsHelper.logEvent("screen_view", mapOf(
                    "screen_name" to "note_editor",
                    "is_new_note" to (noteId == 0L),
                    "note_id" to noteId,
                    "user_id" to userId
                ))
            }

            NoteEditorScreen(
                noteId = noteIdForEditor,
                initialTitle = note?.title ?: "",
                initialContent = note?.description ?: "",
                onBackClick = { navController.popBackStack() },
                onSaveClick = { title, content ->
                    viewModel.addOrUpdateNote(title, content, noteIdForEditor)
                },
                viewModel = viewModel
            )
        }
    }
}
