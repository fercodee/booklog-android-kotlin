package com.example.booklog.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.booklog.data.local.BookDatabase
import com.example.booklog.data.repository.BookRepository
import com.example.booklog.ui.screens.addbook.AddBookScreen
import com.example.booklog.ui.screens.addbook.AddBookViewModel
import com.example.booklog.ui.screens.addbook.AddBookViewModelFactory
import com.example.booklog.ui.screens.bookdetail.BookDetailScreen
import com.example.booklog.ui.screens.bookdetail.BookDetailViewModel
import com.example.booklog.ui.screens.bookdetail.BookDetailViewModelFactory
import com.example.booklog.ui.screens.editbook.EditBookScreen
import com.example.booklog.ui.screens.editbook.EditBookViewModel
import com.example.booklog.ui.screens.editbook.EditBookViewModelFactory
import com.example.booklog.ui.screens.home.HomeScreen
import com.example.booklog.ui.screens.home.HomeViewModel
import com.example.booklog.ui.screens.home.HomeViewModelFactory

@Composable
fun BookLogNavGraph(
    navController: NavHostController,
    database: BookDatabase
) {
    val repository = BookRepository(database.bookDao())

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAddBook = { navController.navigate(Screen.AddBook.route) },
                onNavigateToBookDetail = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                }
            )
        }

        composable(Screen.AddBook.route) {
            val viewModel: AddBookViewModel = viewModel(
                factory = AddBookViewModelFactory(repository)
            )
            AddBookScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: return@composable
            val viewModel: BookDetailViewModel = viewModel(
                factory = BookDetailViewModelFactory(repository, bookId)
            )
            BookDetailScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Screen.EditBook.createRoute(bookId)) }
            )
        }

        composable(
            route = Screen.EditBook.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: return@composable
            val viewModel: EditBookViewModel = viewModel(
                factory = EditBookViewModelFactory(repository, bookId)
            )
            EditBookScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

