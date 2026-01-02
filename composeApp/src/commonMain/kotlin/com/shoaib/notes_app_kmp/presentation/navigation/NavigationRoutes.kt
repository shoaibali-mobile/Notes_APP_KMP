package com.shoaib.notes_app_kmp.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object NotesList : Screen("notes_list")
    object NoteEditor : Screen("note_editor/{noteId}") {
        fun createRoute(noteId: Long? = null): String {
            return if (noteId != null) {
                "note_editor/$noteId"
            } else {
                "note_editor/0"
            }
        }
    }
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
}

