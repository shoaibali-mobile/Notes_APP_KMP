package com.shoaib.notes_app_kmp.domain.repository

import com.shoaib.notes_app_kmp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes():Flow<List<Note>>
    suspend fun addNote(note:Note):Long
}