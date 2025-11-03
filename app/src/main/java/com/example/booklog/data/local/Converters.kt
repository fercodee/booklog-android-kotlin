package com.example.booklog.data.local

import androidx.room.TypeConverter
import com.example.booklog.data.model.BookStatus

class Converters {
    @TypeConverter
    fun fromBookStatus(status: BookStatus): String {
        return status.name
    }

    @TypeConverter
    fun toBookStatus(status: String): BookStatus {
        return BookStatus.valueOf(status)
    }
}

