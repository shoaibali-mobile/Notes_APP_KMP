package com.shoaib.notes_app_kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shoaib.notes_app_kmp.data.local.DatabaseDriverFactory
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NoteEditorScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NotesListScreen
import com.shoaib.notes_app_kmp.presentation.ui.theme.NotesAppTheme
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
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
    val viewModel: NotesViewModel = viewModel()

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
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
