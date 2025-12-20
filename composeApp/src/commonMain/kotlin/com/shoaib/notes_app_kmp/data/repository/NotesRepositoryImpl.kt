package com.shoaib.notes_app_kmp.data.repository

import com.shoaib.notes_app_kmp.data.local.NotesLocalDataSource
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(
    private val localDataSource: NotesLocalDataSource
): NotesRepository {
    override fun getAllNotes(): Flow<List<Note>> {
       return localDataSource.getAllNotes()
    }

    override suspend fun addNote(note: Note): Long {
        return localDataSource.addNote(note)
    }
}