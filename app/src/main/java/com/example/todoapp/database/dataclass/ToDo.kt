package com.example.todoapp.database.dataclass

/**
 * Das ist eine einfache Datenklasse für ToDos.
 *
 * @param id Eindeutige Nummer für jedes ToDo.
 * @param name Name des ToDos.
 * @param description Beschreibung des ToDos.
 * @param priority Die Priorität des ToDos (1 = niedrig, 3 = hoch).
 * @param deadline Bis wann soll das ToDo erledigt werden (als Text, z.B. "2025-01-31").
 * @param status Status des ToDos (0 = offen, 1 = erledigt).
 */
data class ToDo(
    val id: Int = 0,
    val name: String,
    val description: String,
    val priority: Int,
    val deadline: String,
    val status: Int // 0 = offen, 1 = erledigt
) {
    /**
     * Ein praktisches Zusatzfeld, um schnell zu sehen, ob das ToDo fertig ist.
     */
    val isCompleted: Boolean
        get() = status == 1
}
