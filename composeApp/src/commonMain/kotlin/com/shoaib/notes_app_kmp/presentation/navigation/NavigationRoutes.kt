package com.shoaib.notes_app_kmp.presentation.navigation

sealed class Screen(val route: String) {
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
}

