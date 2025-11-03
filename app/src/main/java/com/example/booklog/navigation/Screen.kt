package com.example.booklog.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddBook : Screen("add_book")
    object EditBook : Screen("edit_book/{bookId}") {
        fun createRoute(bookId: Long) = "edit_book/$bookId"
    }
    object BookDetail : Screen("book_detail/{bookId}") {
        fun createRoute(bookId: Long) = "book_detail/$bookId"
    }
}

