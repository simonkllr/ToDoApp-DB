package com.example.todoapp.screen

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.todoapp.database.dataclass.ToDo
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
/**
 * Ein Dialog, mit dem ein bestehendes ToDo bearbeitet werden kann.
 *
 * @param toDo Das ToDo-Objekt, das bearbeitet werden soll.
 * @param onDismiss Callback-Funktion, die aufgerufen wird, wenn der Dialog geschlossen wird.
 * @param onSave Callback-Funktion, die das aktualisierte ToDo zurückgibt, wenn die Änderungen gespeichert werden.
 */
@Composable
fun EditToDoDialog(
    toDo: ToDo,
    onDismiss: () -> Unit,
    onSave: (ToDo) -> Unit
) {
    // Initialwerte der ToDo-Felder
    var name by remember { mutableStateOf(toDo.name) }
    var description by remember { mutableStateOf(toDo.description) }
    var priorityText by remember { mutableStateOf(toDo.priority.toString()) } // Speichere die Priorität als String
    var deadline by remember { mutableStateOf(toDo.deadline) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Konvertiere die Priorität aus dem Textfeld
    val priority = priorityText.toIntOrNull()?.takeIf { it in 1..3 } ?: toDo.priority

    // Validierungslogik: Prüfe, ob alle Felder korrekt ausgefüllt sind
    val isFormValid = name.isNotBlank() && description.isNotBlank() && priorityText.isNotBlank() && deadline.isNotBlank()

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("ToDo bearbeiten") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Eingabefeld für den Namen
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Eingabefeld für die Beschreibung
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Beschreibung") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Eingabefeld für die Priorität
                TextField(
                    value = priorityText,
                    onValueChange = { input ->
                        priorityText = input.filter { it.isDigit() } // Erlaube nur numerische Eingaben
                    },
                    label = { Text("Priorität (1-3)") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Deadline anzeigen oder auswählen
                Text(
                    text = if (deadline.isEmpty()) "Keine Deadline ausgewählt" else "Deadline: $deadline",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = { showDatePicker = true }) {
                    Text("Datum auswählen")
                }
            }

            // DatePicker anzeigen, falls aktiviert
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
                    // Aktualisiere das ToDo-Objekt mit den neuen Werten
                    val updatedToDo = toDo.copy(
                        name = name,
                        description = description,
                        priority = priority, // Aktualisierte Priorität
                        deadline = deadline
                    )
                    onSave(updatedToDo) // Speichere die Änderungen
                    onDismiss() // Schließe den Dialog
                },
                enabled = isFormValid // Button nur aktivieren, wenn das Formular gültig ist
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Abbrechen")
            }
        }
    )
}


/**
 * Ein Dialog, mit dem der Benutzer ein Datum auswählen kann.
 *
 * @param onDateSelected Callback-Funktion, die das ausgewählte Datum im Format "MM/dd/yyyy" zurückgibt.
 * @param onDismiss Callback-Funktion, die aufgerufen wird, wenn der Dialog geschlossen wird.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val datePickerState = rememberDatePickerState()
    val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    val activity = LocalContext.current as? AppCompatActivity

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val selectedDate = dateFormatter.format(Date(selectedMillis))
                            onDateSelected(selectedDate)
                        }
                        onDismiss()
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
