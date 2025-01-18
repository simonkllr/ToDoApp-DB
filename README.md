# ToDoApp - README

## Installationsanleitung

### Voraussetzungen:
-Android Studio
-compileSdk = 35
-minSdk = 26
-targetSdk = 34
-Ein Android-Gerät oder Emulator zur Ausführung der App
-(Getestet auf Pixel 8 Pro API 29)


Lade den Quellcode des Projekts herunter oder klone das Repository mit:
https://github.com/simonkllr/ToDoApp-DB.git
Stelle sicher, dass alle benötigten Abhängigkeiten in der build.gradle-Datei definiert sind.
Sync das Projekt in Android Studio: File > Sync Project with Gradle Files.

Wähle ein Zielgerät (Emulator oder physisches Android-Gerät).
Klicke auf den "Run"-Button in Android Studio.


## Funktionsbeschreibung
Dashboard:
Bietet eine Übersicht über die beiden Hauptbereiche:
Aktive ToDos: Aufgaben, die noch offen sind.
Erledigte ToDos: Aufgaben, die bereits abgeschlossen wurden.

ToDo-Liste:
Zeigt alle Aufgaben entweder als offen oder erledigt, abhängig von der ausgewählten Kategorie.
Jede Aufgabe wird in einer Karte dargestellt, die folgende Informationen enthält:
-Name der Aufgabe
-Beschreibung
-Deadline
-Priorität (1–3)

Aufgabenmanagement:
Hinzufügen von Aufgaben: Dialog zur Erstellung von Aufgaben mit Name, Beschreibung, Priorität und Deadline.
Bearbeiten von Aufgaben: Änderungen an bestehenden Aufgaben sind möglich.
Status ändern: Aufgaben können zwischen offen und erledigt umgeschaltet werden.
Löschen von Aufgaben: Dauerhaftes Entfernen von Aufgaben.

Navigation:
Navigation zwischen Dashboard, aktiven und erledigten Aufgaben mittels Jetpack Compose Navigation.

## Verwendete Technologien

Programmiersprache: Kotlin
Frameworks & Bibliotheken:
-Jetpack Compose: Moderne UI-Entwicklung.
-SQLite: Speicherung der Aufgaben.
-Material Design 3: Für ein modernes App-Design.
Architektur: MVC
-Model: ToDo-Datenklasse und ToDoDbHelper (SQLite).
-View: UI-Komponenten (ToDoScreen, DashboardScreen).
-Controller: ToDoController für Datenbankoperationen.
Build-Tools: Gradle für Abhängigkeits- und Build-Verwaltung.

## Bekannte Probleme
Deadline in die Vergangenheit: Ein Datum aus der Vergangenheit kann zur Deadline hinzugefügt werden
Doppelte Aufgaben: Es gibt keine Prüfung, ob Aufgaben mit gleichem Namen und gleicher Deadline doppelt erstellt werden.
Keine Synchronisation: Die App speichert Aufgaben nur lokal und unterstützt keine Synchronisation mit der Cloud oder anderen Geräten.
