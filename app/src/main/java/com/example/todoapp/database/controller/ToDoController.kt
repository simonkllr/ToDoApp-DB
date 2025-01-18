package com.example.todoapp.database.controller

import android.content.ContentValues
import android.content.Context
import com.example.todoapp.database.ToDoDbHelper
import com.example.todoapp.database.dataclass.ToDo

/**
 * Diese Klasse kümmert sich um alles, was mit der Datenbank für ToDos zu tun hat.
 * Sie hilft, neue ToDos zu speichern, sie auszulesen, zu bearbeiten und zu löschen.
 *
 * @param context Der "Context" ist einfach die Umgebung, die die App aktuell benutzt.
 * Das brauchen wir, um die Datenbank zu starten.
 */
class ToDoController(context: Context) {
    private val dbHelper = ToDoDbHelper(context) // Die Klasse, die die Datenbank erstellt

    /**
     * Fügt ein neues ToDo in die Datenbank ein.
     *
     * @param toDo Das ToDo, das gespeichert werden soll.
     * @return True, wenn es geklappt hat, sonst False.
     */
    fun insertToDo(toDo: ToDo): Boolean {
        val db = dbHelper.writableDatabase // Zugriff auf die Datenbank (Schreiben erlaubt)
        return try {
            val values = ContentValues().apply {
                put("name", toDo.name)
                put("description", toDo.description)
                put("priority", toDo.priority)
                put("deadline", toDo.deadline)
                put("status", toDo.status)
            }
            val result = db.insert("ToDo", null, values)
            result != -1L // Wenn -1, dann ist etwas schiefgelaufen
        } finally {
            db.close() // Datenbank immer wieder schließen!
        }
    }

    /**
     * Holt alle ToDos aus der Datenbank.
     *
     * @return Eine Liste mit allen ToDos.
     */
    fun getAllToDos(): List<ToDo> {
        val db = dbHelper.readableDatabase // Zugriff auf die Datenbank (nur Lesen)
        val todos = mutableListOf<ToDo>() // Liste für die ToDos
        val cursor = db.rawQuery("SELECT * FROM ToDo", null) // Alle Einträge holen
        cursor.use {
            if (cursor.moveToFirst()) { // Check: Gibt es Einträge?
                do {
                    val todo = ToDo(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority")),
                        deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    )
                    todos.add(todo) // ToDo zur Liste hinzufügen
                } while (cursor.moveToNext()) // Zum nächsten Eintrag springen
            }
        }
        return todos
    }




    /**
     * Löscht ein ToDo aus der Datenbank.
     *
     * @param id Die ID des ToDos, das gelöscht werden soll.
     * @return True, wenn es geklappt hat, sonst False.
     */
    fun deleteToDo(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val result = db.delete("ToDo", "id = ?", arrayOf(id.toString()))
            result > 0
        } finally {
            db.close()
        }
    }
    /**
     * Ändert den Status eines ToDos (z.B. von "offen" zu "erledigt").
     *
     * @param id Die ID des ToDos, das geändert werden soll.
     * @param newStatus Der neue Status (0 = offen, 1 = erledigt).
     * @return True, wenn es geklappt hat, sonst False.
     */
    fun updateToDo(toDo: ToDo): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", toDo.name)
                put("description", toDo.description)
                put("priority", toDo.priority)
                put("deadline", toDo.deadline)
                put("status", toDo.status)
            }
            val result = db.update("ToDo", values, "id = ?", arrayOf(toDo.id.toString()))
            result > 0
        } finally {
            db.close()
        }
    }
}
