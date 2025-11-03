package com.example.booklog.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.booklog.data.model.Book
import com.example.booklog.data.model.BookStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAddBook: () -> Unit,
    onNavigateToBookDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BookLog", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                ),
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            Icons.Default.FilterAlt,
                            contentDescription = "Filtrar",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showSortDialog = true }) {
                        Icon(
                            Icons.Default.SwapVert,
                            contentDescription = "Ordenar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddBook,
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar livro")
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar por título ou autor...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar")
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                singleLine = true
            )

            // Active filters display
            if (uiState.selectedStatus != null || uiState.selectedGenre != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.selectedStatus?.let { status ->
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.onStatusFilterChange(null) },
                            label = { Text(getStatusLabel(status)) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remover filtro",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }

                    uiState.selectedGenre?.let { genre ->
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.onGenreFilterChange(null) },
                            label = { Text(genre) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remover filtro",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
            }

            // Books list
            if (uiState.filteredBooks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ImportContacts,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (uiState.books.isEmpty())
                                "Nenhum livro cadastrado"
                            else
                                "Nenhum livro encontrado",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredBooks, key = { it.id }) { book ->
                        BookItem(
                            book = book,
                            onClick = { onNavigateToBookDetail(book.id) }
                        )
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        FilterDialog(
            currentStatus = uiState.selectedStatus,
            currentGenre = uiState.selectedGenre,
            genres = uiState.genres,
            onStatusSelected = { viewModel.onStatusFilterChange(it) },
            onGenreSelected = { viewModel.onGenreFilterChange(it) },
            onDismiss = { showFilterDialog = false }
        )
    }

    if (showSortDialog) {
        SortDialog(
            currentSort = uiState.sortBy,
            onSortSelected = {
                viewModel.onSortChange(it)
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }
}

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Book cover
            val imageModel = book.coverUrl ?: book.coverUri
            if (imageModel != null) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = "Capa de ${book.title}",
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ImportContacts,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Book info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book.genre,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Status badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = getStatusColor(book.status)
                    ) {
                        Text(
                            text = getStatusLabel(book.status),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }

                    // Rating
                    if (book.rating > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFA000),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = book.rating.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    currentStatus: BookStatus?,
    currentGenre: String?,
    genres: List<String>,
    onStatusSelected: (BookStatus?) -> Unit,
    onGenreSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar Livros") },
        text = {
            Column {
                Text("Status", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                BookStatus.entries.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStatusSelected(status) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentStatus == status,
                            onClick = { onStatusSelected(status) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(getStatusLabel(status))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStatusSelected(null) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentStatus == null,
                        onClick = { onStatusSelected(null) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Todos")
                }

                if (genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Gênero", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    genres.forEach { genre ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onGenreSelected(genre) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentGenre == genre,
                                onClick = { onGenreSelected(genre) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(genre)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onGenreSelected(null) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentGenre == null,
                            onClick = { onGenreSelected(null) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Todos")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

@Composable
fun SortDialog(
    currentSort: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ordenar Por") },
        text = {
            Column {
                SortOption.entries.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSortSelected(option) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSort == option,
                            onClick = { onSortSelected(option) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(getSortLabel(option))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

fun getStatusLabel(status: BookStatus): String {
    return when (status) {
        BookStatus.PARA_LER -> "Para Ler"
        BookStatus.LENDO -> "Lendo"
        BookStatus.LIDO -> "Lido"
    }
}

fun getStatusColor(status: BookStatus): Color {
    return when (status) {
        BookStatus.PARA_LER -> Color(0xFF757575)
        BookStatus.LENDO -> Color(0xFF2196F3)
        BookStatus.LIDO -> Color(0xFF4CAF50)
    }
}

fun getSortLabel(option: SortOption): String {
    return when (option) {
        SortOption.TITLE_ASC -> "Título (A-Z)"
        SortOption.TITLE_DESC -> "Título (Z-A)"
        SortOption.AUTHOR_ASC -> "Autor (A-Z)"
        SortOption.AUTHOR_DESC -> "Autor (Z-A)"
        SortOption.UPDATED_DESC -> "Modificado Recentemente"
        SortOption.UPDATED_ASC -> "Modificado Antigamente"
        SortOption.RATING_DESC -> "Avaliação (Maior)"
        SortOption.RATING_ASC -> "Avaliação (Menor)"
    }
}

