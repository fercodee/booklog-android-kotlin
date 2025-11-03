package com.example.booklog.data.repository

import com.example.booklog.data.local.BookDao
import com.example.booklog.data.model.Book
import com.example.booklog.data.model.BookStatus
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    suspend fun getBookById(bookId: Long): Book? = bookDao.getBookById(bookId)

    fun getBooksByStatus(status: BookStatus): Flow<List<Book>> =
        bookDao.getBooksByStatus(status)

    fun getBooksByGenre(genre: String): Flow<List<Book>> =
        bookDao.getBooksByGenre(genre)

    fun searchBooks(query: String): Flow<List<Book>> =
        bookDao.searchBooks(query)

    suspend fun insertBook(book: Book): Long = bookDao.insertBook(book)

    suspend fun updateBook(book: Book) = bookDao.updateBook(book)

    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)

    fun getAllGenres(): Flow<List<String>> = bookDao.getAllGenres()
}

