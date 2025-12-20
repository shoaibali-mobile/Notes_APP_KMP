package com.shoaib.notes_app_kmp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shoaib.notes_app_kmp.domain.model.Note
import com.shoaib.notes_app_kmp.util.getCurrentTimeMillis


@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val createdAt: Long = getCurrentTimeMillis()
)
{
    fun toDomain(): Note {
        return Note(
            id = id,
            title = title,
            description = description,
            createdAt = createdAt
        )
    }
}


fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        description = description,
        createdAt = createdAt
    )
}