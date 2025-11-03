package com.example.booklog.ui.screens.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booklog.data.model.Book
import com.example.booklog.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookDetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val isDeleted: Boolean = false
)

class BookDetailViewModel(
    private val repository: BookRepository,
    private val bookId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val book = repository.getBookById(bookId)
            _uiState.update {
                it.copy(
                    book = book,
                    isLoading = false
                )
            }
        }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteBook() {
        viewModelScope.launch {
            _uiState.value.book?.let { book ->
                repository.deleteBook(book)
                _uiState.update { it.copy(isDeleted = true, showDeleteDialog = false) }
            }
        }
    }
}

class BookDetailViewModelFactory(
    private val repository: BookRepository,
    private val bookId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(repository, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

