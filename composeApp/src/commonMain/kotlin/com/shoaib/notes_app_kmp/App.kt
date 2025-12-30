package com.shoaib.notes_app_kmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NoteEditorScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NotesListScreen
import com.shoaib.notes_app_kmp.presentation.ui.theme.NotesAppTheme
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import org.koin.compose.koinInject

@Composable
fun App() {
    NotesAppTheme {
        AppContent()
    }
}


@Composable
private fun AppContent() {
    val navController = rememberNavController()
    val viewModel: NotesViewModel = koinInject()

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
