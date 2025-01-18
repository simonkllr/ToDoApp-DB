package com.example.todoapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.database.controller.ToDoController
import com.example.todoapp.screen.DashboardScreen
import com.example.todoapp.screen.ToDoScreen
/**
 * Die zentrale Navigationsfunktion der App. Sie definiert die verschiedenen "Seiten"
 * (oder Screens), zwischen denen der Nutzer navigieren kann, und wie diese aufgebaut sind.
 *
 * Die Navigation basiert auf **Jetpack Compose Navigation**.
 */
@Composable
fun AppNavHost() {
    // Erstellt einen Navigation Controller, der für die Navigation zwischen den Screens genutzt wird.
    val navController = rememberNavController()

    // Definiert die Navigation innerhalb der App. Die Startseite ist "dashboard".
    NavHost(navController = navController, startDestination = "dashboard") {

        /**
         * Die "dashboard"-Route:
         * Zeigt das Dashboard mit zwei Optionen:
         * - Aktive ToDos
         * - Erledigte ToDos
         *
         * Beim Klicken auf eine Option navigiert man zu einer der ToDo-Seiten.
         */
        composable("dashboard") {
            DashboardScreen(navController) // Zeigt das Dashboard an
        }

        /**
         * Die "active_todos"-Route:
         * Zeigt alle aktiven (offenen) ToDos an.
         *
         * - Holt die Liste der ToDos aus der Datenbank, filtert sie nach Status = 0 (offen).
         * - Leitet die Liste und den Status an `ToDoScreen` weiter.
         */
        composable("active_todos") {
            val todos = ToDoController(LocalContext.current).getAllToDos().filter { it.status == 0 }
            ToDoScreen(
                todos = todos, // Liste der aktiven ToDos
                navController = navController, // Navigation Controller
                status = 0 // Status 0 = offen
            )
        }

        /**
         * Die "completed_todos"-Route:
         * Zeigt alle erledigten ToDos an.
         *
         * - Holt die Liste der ToDos aus der Datenbank, filtert sie nach Status = 1 (erledigt).
         * - Leitet die Liste und den Status an `ToDoScreen` weiter.
         */
        composable("completed_todos") {
            val todos = ToDoController(LocalContext.current).getAllToDos().filter { it.status == 1 }
            ToDoScreen(
                todos = todos, // Liste der erledigten ToDos
                navController = navController, // Navigation Controller
                status = 1 // Status 1 = erledigt
            )
        }
    }
}
