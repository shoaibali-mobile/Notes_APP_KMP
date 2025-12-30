package com.shoaib.notes_app_kmp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val username: String ,
    val passwordHash: String
)
