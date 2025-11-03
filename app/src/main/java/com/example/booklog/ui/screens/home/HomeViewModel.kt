package com.example.booklog.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booklog.data.model.Book
import com.example.booklog.data.model.BookStatus
import com.example.booklog.data.repository.BookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val books: List<Book> = emptyList(),
    val filteredBooks: List<Book> = emptyList(),
    val selectedStatus: BookStatus? = null,
    val selectedGenre: String? = null,
    val searchQuery: String = "",
    val genres: List<String> = emptyList(),
    val sortBy: SortOption = SortOption.UPDATED_DESC
)

enum class SortOption {
    TITLE_ASC,
    TITLE_DESC,
    AUTHOR_ASC,
    AUTHOR_DESC,
    UPDATED_DESC,
    UPDATED_ASC,
    RATING_DESC,
    RATING_ASC
}

class HomeViewModel(private val repository: BookRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
        loadGenres()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            repository.getAllBooks().collect { books ->
                _uiState.update { currentState ->
                    currentState.copy(
                        books = books,
                        filteredBooks = applyFiltersAndSort(
                            books,
                            currentState.selectedStatus,
                            currentState.selectedGenre,
                            currentState.searchQuery,
                            currentState.sortBy
                        )
                    )
                }
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            repository.getAllGenres().collect { genres ->
                _uiState.update { it.copy(genres = genres) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredBooks = applyFiltersAndSort(
                    currentState.books,
                    currentState.selectedStatus,
                    currentState.selectedGenre,
                    query,
                    currentState.sortBy
                )
            )
        }
    }

    fun onStatusFilterChange(status: BookStatus?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedStatus = status,
                filteredBooks = applyFiltersAndSort(
                    currentState.books,
                    status,
                    currentState.selectedGenre,
                    currentState.searchQuery,
                    currentState.sortBy
                )
            )
        }
    }

    fun onGenreFilterChange(genre: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedGenre = genre,
                filteredBooks = applyFiltersAndSort(
                    currentState.books,
                    currentState.selectedStatus,
                    genre,
                    currentState.searchQuery,
                    currentState.sortBy
                )
            )
        }
    }

    fun onSortChange(sortOption: SortOption) {
        _uiState.update { currentState ->
            currentState.copy(
                sortBy = sortOption,
                filteredBooks = applyFiltersAndSort(
                    currentState.books,
                    currentState.selectedStatus,
                    currentState.selectedGenre,
                    currentState.searchQuery,
                    sortOption
                )
            )
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }

    private fun applyFiltersAndSort(
        books: List<Book>,
        status: BookStatus?,
        genre: String?,
        query: String,
        sortBy: SortOption
    ): List<Book> {
        var result = books

        // Apply status filter
        if (status != null) {
            result = result.filter { it.status == status }
        }

        // Apply genre filter
        if (!genre.isNullOrBlank()) {
            result = result.filter { it.genre.equals(genre, ignoreCase = true) }
        }

        // Apply search query
        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.author.contains(query, ignoreCase = true)
            }
        }

        // Apply sorting
        result = when (sortBy) {
            SortOption.TITLE_ASC -> result.sortedBy { it.title.lowercase() }
            SortOption.TITLE_DESC -> result.sortedByDescending { it.title.lowercase() }
            SortOption.AUTHOR_ASC -> result.sortedBy { it.author.lowercase() }
            SortOption.AUTHOR_DESC -> result.sortedByDescending { it.author.lowercase() }
            SortOption.UPDATED_DESC -> result.sortedByDescending { it.updatedAt }
            SortOption.UPDATED_ASC -> result.sortedBy { it.updatedAt }
            SortOption.RATING_DESC -> result.sortedByDescending { it.rating }
            SortOption.RATING_ASC -> result.sortedBy { it.rating }
        }

        return result
    }
}

class HomeViewModelFactory(
    private val repository: BookRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

