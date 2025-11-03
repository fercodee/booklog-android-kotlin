package com.example.booklog.ui.screens.editbook

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booklog.data.model.Book
import com.example.booklog.data.model.BookStatus
import com.example.booklog.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditBookUiState(
    val book: Book? = null,
    val title: String = "",
    val author: String = "",
    val genre: String = "",
    val status: BookStatus = BookStatus.PARA_LER,
    val rating: Int = 0,
    val coverUri: String? = null,
    val coverUrl: String = "",
    val notes: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class EditBookViewModel(
    private val repository: BookRepository,
    private val bookId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditBookUiState())
    val uiState: StateFlow<EditBookUiState> = _uiState.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val book = repository.getBookById(bookId)
            if (book != null) {
                _uiState.update {
                    it.copy(
                        book = book,
                        title = book.title,
                        author = book.author,
                        genre = book.genre,
                        status = book.status,
                        rating = book.rating,
                        coverUri = book.coverUri,
                        coverUrl = book.coverUrl ?: "",
                        notes = book.notes ?: "",
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title, errorMessage = null) }
    }

    fun onAuthorChange(author: String) {
        _uiState.update { it.copy(author = author, errorMessage = null) }
    }

    fun onGenreChange(genre: String) {
        _uiState.update { it.copy(genre = genre, errorMessage = null) }
    }

    fun onStatusChange(status: BookStatus) {
        _uiState.update { it.copy(status = status) }
    }

    fun onRatingChange(rating: Int) {
        _uiState.update { it.copy(rating = rating.coerceIn(0, 5)) }
    }

    fun onCoverUriChange(uri: Uri?) {
        _uiState.update { it.copy(coverUri = uri?.toString()) }
    }

    fun onCoverUrlChange(url: String) {
        _uiState.update { it.copy(coverUrl = url) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveBook() {
        val currentState = _uiState.value
        val originalBook = currentState.book ?: return

        if (currentState.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "O título é obrigatório") }
            return
        }

        if (currentState.author.isBlank()) {
            _uiState.update { it.copy(errorMessage = "O autor é obrigatório") }
            return
        }

        if (currentState.genre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "O gênero é obrigatório") }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val updatedBook = originalBook.copy(
                    title = currentState.title.trim(),
                    author = currentState.author.trim(),
                    genre = currentState.genre.trim(),
                    status = currentState.status,
                    rating = currentState.rating,
                    coverUri = currentState.coverUri,
                    coverUrl = currentState.coverUrl.trim().takeIf { it.isNotBlank() },
                    notes = currentState.notes.trim().takeIf { it.isNotBlank() },
                    updatedAt = System.currentTimeMillis()
                )

                repository.updateBook(updatedBook)
                _uiState.update { it.copy(isSaving = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Erro ao atualizar livro: ${e.message}"
                    )
                }
            }
        }
    }
}

class EditBookViewModelFactory(
    private val repository: BookRepository,
    private val bookId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditBookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditBookViewModel(repository, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

