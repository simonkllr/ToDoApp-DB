package com.example.todoapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.database.dataclass.ToDo



/**
 * Ein Dialog, mit dem ein neues ToDo erstellt werden kann.
 *
 * @param onDismiss Wird aufgerufen, wenn der Dialog geschlossen werden soll.
 * @param onSave Wird aufgerufen, wenn das neue ToDo erstellt wurde. Übergibt das neue ToDo.
 */
@Composable
fun AddToDoDialog(
    onDismiss: () -> Unit,
    onSave: (ToDo) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priorityText by remember { mutableStateOf("1") } // Speichert den Textwert direkt
    var deadline by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Konvertiere den Textwert der Priorität in eine Zahl
    val priority = priorityText.toIntOrNull()?.takeIf { it in 1..3 } ?: 1

    // Validierungslogik: Ist das Formular korrekt ausgefüllt?
    val isFormValid = name.isNotBlank() && description.isNotBlank() && priorityText.isNotBlank() && priority in 1..3 && deadline.isNotBlank()

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Neues ToDo hinzufügen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Beschreibung") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = priorityText,
                    onValueChange = { priorityText = it }, // Aktualisiere den Text direkt
                    label = { Text("Priorität (1-3)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = if (deadline.isEmpty()) "Keine Deadline ausgewählt" else "Deadline: $deadline",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = { showDatePicker = true }) {
                    Text("Datum auswählen")
                }
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDateSelected = { selectedDate ->
                        deadline = selectedDate
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newToDo = ToDo(
                        name = name,
                        description = description,
                        priority = priority, // Verwende die konvertierte Priorität
                        deadline = deadline,
                        status = 0
                    )
                    onSave(newToDo) // Speichere das neue ToDo
                    onDismiss()
                },
                enabled = isFormValid // Button ist nur aktiv, wenn das Formular korrekt ist
            ) {
                Text("Erstellen")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Abbrechen")
            }
        }
    )
}
