package com.example.todoapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.todoapp.database.controller.ToDoController
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.todoapp.database.dataclass.ToDo


/**
 * Diese Seite zeigt entweder offene oder erledigte ToDos.
 *
 * @param todos Die Liste der ToDos, die angezeigt werden soll.
 * @param navController Der Navigation Controller, um z.B. zurück zu wechseln.
 * @param status Zeigt an, ob wir offene (0) oder erledigte (1) ToDos anzeigen.
 */
@Composable
fun ToDoScreen(
    todos: List<ToDo>,
    navController: NavHostController,
    status: Int
) {
    val context = LocalContext.current
    val toDoController = ToDoController(context)
    var updatedTodos by remember { mutableStateOf(todos) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var todoToEdit by remember { mutableStateOf<ToDo?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(updatedTodos) { todo ->
                ToDoItemCard(
                    item = todo,
                    onItemClicked = { item ->
                        val newStatus = if (item.isCompleted) 0 else 1
                        toDoController.updateToDo(item.copy(status = newStatus))
                        updatedTodos = toDoController.getAllToDos().filter { it.status == status }
                    },
                    onDeleteClicked = { item ->
                        toDoController.deleteToDo(item.id)
                        updatedTodos = toDoController.getAllToDos().filter { it.status == status }
                    },
                    onEditClicked = { item ->
                        todoToEdit = item
                        showEditDialog = true
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add New ToDo",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        // Zeigt den Dialog zum Hinzufügen eines neuen ToDos an
        if (showAddDialog) {
            AddToDoDialog(
                onDismiss = { showAddDialog = false }, // Schließt den Dialog
                onSave = { newToDo ->
                    toDoController.insertToDo(newToDo) // Speichert das neue ToDo in der Datenbank
                    updatedTodos = toDoController.getAllToDos().filter { it.status == status } // Aktualisiert die Liste
                    showAddDialog = false // Schließt den Dialog nach dem Speichern
                }
            )
        }
    }

    if (showEditDialog && todoToEdit != null) {
        EditToDoDialog(
            toDo = todoToEdit!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedToDo ->
                toDoController.updateToDo(updatedToDo)
                updatedTodos = toDoController.getAllToDos().filter { it.status == status }
                showEditDialog = false
            }
        )
    }
}



/**
 * Eine Karte, die ein einzelnes ToDo anzeigt. Die Karte bietet die Möglichkeit, das ToDo
 * zu bearbeiten, zu löschen oder den Status (erledigt/offen) zu ändern.
 *
 * @param item Das ToDo-Objekt, das angezeigt werden soll.
 * @param onItemClicked Eine Funktion, die aufgerufen wird, wenn auf den Status-Button geklickt wird.
 *        Beispiel: Markiere das ToDo als "erledigt" oder "nicht erledigt".
 * @param onDeleteClicked Eine Funktion, die aufgerufen wird, wenn auf den Löschen-Button geklickt wird.
 *        Beispiel: Entferne das ToDo aus der Liste.
 * @param onEditClicked Eine Funktion, die aufgerufen wird, wenn auf den Bearbeiten-Button geklickt wird.
 *        Beispiel: Öffne einen Dialog, um die ToDo-Daten zu bearbeiten.
 */
@Composable
fun ToDoItemCard(
    item: ToDo,
    onItemClicked: (ToDo) -> Unit,
    onDeleteClicked: (ToDo) -> Unit,
    onEditClicked: (ToDo) -> Unit
) {
    // Eine Karte, die das ToDo visuell darstellt.
    Card(
        modifier = Modifier
            .fillMaxWidth() // Die Karte nimmt die gesamte Breite ein.
            .padding(8.dp), // Abstand um die Karte herum.
        elevation = CardDefaults.cardElevation(4.dp) // Schatteneffekt für die Karte.
    ) {
        // Inhalt der Karte wird in einer Zeile (Row) angeordnet.
        Row(
            modifier = Modifier
                .fillMaxWidth() // Die Zeile nimmt die gesamte Breite ein.
                .padding(16.dp), // Abstand innerhalb der Karte.
            verticalAlignment = Alignment.CenterVertically // Zentriert den Inhalt vertikal.
        ) {
            // Linker Bereich: Beschreibung des ToDos.
            Column(modifier = Modifier.weight(1f)) {
                // Name des ToDos.
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )
                // Beschreibung des ToDos.
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                // Deadline des ToDos.
                Text(
                    text = "Deadline: ${item.deadline}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Mittlerer Bereich: Status-Icon (z.B. "erledigt" oder "offen").
            IconButton(onClick = { onItemClicked(item) }) {
                Icon(
                    imageVector = if (item.isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Done,
                    contentDescription = if (item.isCompleted) "Mark as Not Done" else "Mark as Done",
                    tint = if (item.isCompleted)
                        MaterialTheme.colorScheme.secondary // Erledigt: Sekundärfarbe.
                    else
                        MaterialTheme.colorScheme.primary // Offen: Primärfarbe.
                )
            }

            // Rechter Bereich: Bearbeiten-Icon.
            IconButton(onClick = { onEditClicked(item) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit ToDo",
                    tint = MaterialTheme.colorScheme.primary // Bearbeiten: Primärfarbe.
                )
            }

            // Rechter Bereich: Löschen-Icon.
            IconButton(onClick = { onDeleteClicked(item) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete ToDo",
                    tint = MaterialTheme.colorScheme.error // Löschen: Fehlerfarbe (rot).
                )
            }
        }
    }
}
