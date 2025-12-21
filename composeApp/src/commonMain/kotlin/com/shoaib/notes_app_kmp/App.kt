package com.shoaib.notes_app_kmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shoaib.notes_app_kmp.di.appModule
import com.shoaib.notes_app_kmp.isPlatformContextInitialized
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NoteEditorScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NotesListScreen
import com.shoaib.notes_app_kmp.presentation.ui.theme.NotesAppTheme
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import kotlinx.coroutines.delay
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject


@Composable
fun App() {
    var isContextReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (!isPlatformContextInitialized()) {
            delay(10)
        }
        isContextReady = true
    }

    if (isContextReady) {
        KoinApplication(
            application = {
                modules(appModule)
            }
        ) {
            NotesAppTheme {
                AppContent()
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Loading state - context not ready yet
        }
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
