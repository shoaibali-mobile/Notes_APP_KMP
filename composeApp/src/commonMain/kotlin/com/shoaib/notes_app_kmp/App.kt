package com.shoaib.notes_app_kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shoaib.notes_app_kmp.data.model.BottomBarConfig
import com.shoaib.notes_app_kmp.presentation.components.BottomBar
import com.shoaib.notes_app_kmp.presentation.navigation.Screen
import com.shoaib.notes_app_kmp.presentation.screens.auth.LoginScreen
import com.shoaib.notes_app_kmp.presentation.screens.auth.SignupScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NoteEditorScreen
import com.shoaib.notes_app_kmp.presentation.screens.notes.NotesListScreen
import com.shoaib.notes_app_kmp.presentation.screens.placeholder.PlaceholderScreen
import com.shoaib.notes_app_kmp.presentation.ui.theme.NotesAppTheme
import com.shoaib.notes_app_kmp.presentation.viewmodel.NotesViewModel
import com.shoaib.notes_app_kmp.util.AnalyticsHelper
import com.shoaib.notes_app_kmp.util.createRemoteConfigHelper
import com.shoaib.notes_app_kmp.util.logD
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

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
    
    // Get ViewModel from Koin using koinInject
    val viewModel: NotesViewModel = koinInject()

    // Remote Config state - Initialize with default values so bottom bar shows immediately
    val remoteConfigHelper = remember { createRemoteConfigHelper() }
    var bottomBarConfig by remember { 
        mutableStateOf(BottomBarConfig(home = true, settings = true, profile = true)) 
    }

    // Initialize and fetch Remote Config
    LaunchedEffect(Unit) {
        remoteConfigHelper.initialize()
        remoteConfigHelper.fetchAndActivate { activated ->
            // Always read the config value, even if not newly activated
            // (it might be using cached values that are still valid)
            val configJson = remoteConfigHelper.getString(
                "bottom_bar_config", 
                """{"home": true, "settings": true, "profile": true}"""
            )
            
            // Add logging to debug
            logD("RemoteConfigHelper", "Retrieved bottom_bar_config: $configJson")
            
            try {
                val newConfig = Json.decodeFromString<BottomBarConfig>(configJson)
                bottomBarConfig = newConfig
                logD("RemoteConfigHelper", "Bottom bar config updated: home=${newConfig.home}, settings=${newConfig.settings}, profile=${newConfig.profile}")
            } catch (e: Exception) {
                logD("RemoteConfigHelper", "Failed to parse bottom_bar_config: ${e.message}")
                // Keep default config if parsing fails
            }
        }
    }

    // Get current route to conditionally show bottom bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Check if any bottom bar item is enabled
    val hasAnyBottomBarItem = bottomBarConfig.home || bottomBarConfig.settings || bottomBarConfig.profile
    
    // Show bottom bar only on main screens (not on login/signup/note editor) AND if at least one item is enabled
    val showBottomBar = currentRoute != null && 
        currentRoute != Screen.Login.route && 
        currentRoute != Screen.Signup.route &&
        !currentRoute.startsWith(Screen.NoteEditor.route.split("/")[0]) &&
        hasAnyBottomBarItem

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    config = bottomBarConfig,
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Login Screen
            composable(Screen.Login.route) {
                LaunchedEffect(Unit) {
                    AnalyticsHelper.logEvent("screen_view", mapOf(
                        "screen_name" to "login"
                    ))
                }
                LoginScreen(navController = navController)
            }

            // Signup Screen
            composable(Screen.Signup.route) {
                LaunchedEffect(Unit) {
                    AnalyticsHelper.logEvent("screen_view", mapOf(
                        "screen_name" to "signup"
                    ))
                }
                SignupScreen(navController = navController)
            }

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
                    },
                    viewModel = viewModel
                )
            }

            // Placeholder screens for bottom bar items
            composable(Screen.Home.route) {
                LaunchedEffect(Unit) {
                    AnalyticsHelper.logEvent("screen_view", mapOf(
                        "screen_name" to "home"
                    ))
                }
                PlaceholderScreen(
                    screenName = "Home",
                    icon = Icons.Default.Home
                )
            }

            composable(Screen.Settings.route) {
                LaunchedEffect(Unit) {
                    AnalyticsHelper.logEvent("screen_view", mapOf(
                        "screen_name" to "settings"
                    ))
                }
                PlaceholderScreen(
                    screenName = "Settings",
                    icon = Icons.Default.Settings
                )
            }

            composable(Screen.Profile.route) {
                LaunchedEffect(Unit) {
                    AnalyticsHelper.logEvent("screen_view", mapOf(
                        "screen_name" to "profile"
                    ))
                }
                PlaceholderScreen(
                    screenName = "Profile",
                    icon = Icons.Default.Person
                )
            }
        }
    }
}
