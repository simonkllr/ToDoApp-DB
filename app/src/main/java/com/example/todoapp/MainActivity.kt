package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.todoapp.ui.theme.ToDoAppTheme

/**
 * Die Hauptaktivität der App. Sie startet alles.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                // Setze das Hauptlayout
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost() // Verwende das AppNavHost für die Navigation


                }
            }
        }
    }
}
