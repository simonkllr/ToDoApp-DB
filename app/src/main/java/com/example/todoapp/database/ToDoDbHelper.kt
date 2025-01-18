package com.example.todoapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Hilft dabei, die Datenbank zu erstellen, zu updaten oder zu löschen.
 *
 * @param context Der aktuelle Zustand der App.
 */
class ToDoDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Wird beim ersten Start der App aufgerufen, um die ToDo-Tabelle zu erstellen.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
            CREATE TABLE ToDo (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                priority INTEGER NOT NULL,
                deadline TEXT NOT NULL,
                description TEXT,
                status INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }

    /**
     * Wird aufgerufen, wenn die Datenbank aktualisiert werden muss.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ToDo") // Alte Tabelle löschen
        onCreate(db) // Neue Tabelle erstellen
    }

    companion object {
        const val DATABASE_NAME = "ToDoApp.db" // Name der Datenbankdatei
        const val DATABASE_VERSION = 1 // Version der Datenbank
    }
}
