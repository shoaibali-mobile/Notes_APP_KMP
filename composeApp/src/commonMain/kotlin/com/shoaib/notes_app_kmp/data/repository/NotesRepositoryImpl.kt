package com.shoaib.notes_app_kmp.data.repository

import com.shoaib.notes_app_kmp.data.local.NotesLocalDataSource
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.domain.repository.NotesRepository
import com.shoaib.notes_app_kmp.util.CrashlyticsHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class NotesRepositoryImpl(
    private val localDataSource: NotesLocalDataSource
): NotesRepository {
    override fun getAllNotes(): Flow<List<Note>> {
       return localDataSource.getAllNotes()
           .catch { exception ->
               // Log error to Crashlytics
               CrashlyticsHelper.recordException(exception)
               CrashlyticsHelper.log("Error fetching notes: ${exception.message}")
               // Emit empty list on error
               emit(emptyList())
           }
    }

    override suspend fun addNote(note: Note): Long {
        return try {
            localDataSource.addNote(note)
        } catch (e: Exception) {
            // Log error to Crashlytics
            CrashlyticsHelper.recordException(e)
            CrashlyticsHelper.log("Error adding note: ${e.message}, Note ID: ${note.id}")
            throw e
        }
    }
}