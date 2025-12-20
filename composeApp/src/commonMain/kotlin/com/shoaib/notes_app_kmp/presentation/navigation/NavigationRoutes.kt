package com.shoaib.notes_app_kmp.presentation.navigation

sealed class Screen(val route: String) {
    object NotesList : Screen("notes_list")
    object NoteEditor : Screen("note_editor")
}

