package com.shoaib.notes_app_kmp.data.local

import com.shoaib.notes_app_kmp.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesLocalDataSource(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
        }

    suspend fun addNote(note: Note): Long {
        return noteDao.insertNote(note.toEntity())
    }

}

