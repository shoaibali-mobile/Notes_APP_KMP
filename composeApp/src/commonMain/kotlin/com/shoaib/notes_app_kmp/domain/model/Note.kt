package com.shoaib.notes_app_kmp.domain.model

import com.shoaib.notes_app_kmp.util.getCurrentTimeMillis

data class Note(
    val id: Long = 0,
    val title: String,
    val description: String,
    val createdAt: Long = getCurrentTimeMillis()
)
