package com.example.booklog.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String,
    val genre: String,
    val status: BookStatus,
    val rating: Int = 0, // 0-5
    val coverUri: String? = null,
    val coverUrl: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class BookStatus {
    PARA_LER,
    LENDO,
    LIDO
}

