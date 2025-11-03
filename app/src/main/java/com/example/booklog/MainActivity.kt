package com.example.booklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.booklog.data.local.BookDatabase
import com.example.booklog.navigation.BookLogNavGraph
import com.example.booklog.ui.theme.BookLogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = BookDatabase.getDatabase(applicationContext)

        setContent {
            BookLogTheme {
                val navController = rememberNavController()
                BookLogNavGraph(
                    navController = navController,
                    database = database
                )
            }
        }
    }
}
