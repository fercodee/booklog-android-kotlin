package com.example.booklog.data.local

import androidx.room.*
import com.example.booklog.data.model.Book
import com.example.booklog.data.model.BookStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY updatedAt DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Long): Book?

    @Query("SELECT * FROM books WHERE status = :status ORDER BY updatedAt DESC")
    fun getBooksByStatus(status: BookStatus): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE genre = :genre ORDER BY updatedAt DESC")
    fun getBooksByGenre(genre: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchBooks(query: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT DISTINCT genre FROM books ORDER BY genre ASC")
    fun getAllGenres(): Flow<List<String>>
}

