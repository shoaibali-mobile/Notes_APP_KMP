package com.shoaib.notes_app_kmp.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object NotesList : Screen("notes_list/{userId}") {
        fun createRoute(userId: Int): String = "notes_list/$userId"
    }
    object NoteEditor : Screen("note_editor/{userId}/{noteId}") {
        fun createRoute(userId: Int, noteId: Long? = null): String {
            return if (noteId != null) {
                "note_editor/$userId/$noteId"
            } else {
                "note_editor/$userId/0"
            }
        }
    }
}

