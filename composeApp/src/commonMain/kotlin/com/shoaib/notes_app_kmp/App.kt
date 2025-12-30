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
            NotesListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.NoteEditor.route) {
            NoteEditorScreen(
                onBackClick = { navController.popBackStack() },
                viewModel=viewModel
            )
        }
    }
}
