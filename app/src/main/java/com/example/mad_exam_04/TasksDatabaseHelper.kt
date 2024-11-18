package co.sanchez.proyecto_kotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TasksDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "task.db"
        private const val DATABASE_VERSION = 1 // Incrementar la versión de la base de datos
        private const val TABLE_NAME = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_COMPLETION_DATE = "completion_date"
        private const val COLUMN_CREATION_DATE = "creationDate"
        private const val COLUMN_PRIORITY = "priority"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_CONTENT TEXT,
                $COLUMN_COMPLETION_DATE TEXT, -- Nuevo campo
                $COLUMN_CREATION_DATE TEXT,-- Nuevo campo
                $COLUMN_PRIORITY TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val addCompletionDateColumn = "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_COMPLETION_DATE TEXT"
            val addCreationDateColumn = "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_CREATION_DATE TEXT" // Agregar el campo de fecha de creación
            db?.execSQL(addCompletionDateColumn)
            db?.execSQL(addCreationDateColumn) // Añadir la columna de fecha de creación
        }
    }

    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_CONTENT, task.content)
            put(COLUMN_COMPLETION_DATE, task.completionDate) // Nuevo campo
            put(COLUMN_CREATION_DATE, task.creationDate) // Nuevo campo
            put(COLUMN_PRIORITY, task.priority)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getTasksByPriority(priority: String): List<Task> {
        val tasksList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_PRIORITY = ?"
        val cursor = db.rawQuery(query, arrayOf(priority))

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val completionDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETION_DATE))
            val creationDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATION_DATE))
            val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))

            val task = Task(id, title, content, completionDate, creationDate, priority)
            tasksList.add(task)
        }
        cursor.close()
        db.close()
        return tasksList
    }


    fun getAllTasks(): List<Task> {
        val tasksList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val completionDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETION_DATE))
            val creationDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATION_DATE))
            val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))

            val task = Task(id, title, content, completionDate, creationDate, priority)
            tasksList.add(task)
        }
        cursor.close()
        db.close()
        return tasksList
    }

    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_CONTENT, task.content)
            put(COLUMN_COMPLETION_DATE, task.completionDate)
            put(COLUMN_PRIORITY, task.priority)
            // No se necesita actualizar la fecha de creación ya que es automática
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getTaskByID(taskId: Int): Task {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val completionDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETION_DATE))
        val creationDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATION_DATE))
        val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))

        cursor.close()
        db.close()
        return Task(id, title, content, completionDate, creationDate, priority)
    }

    fun deleteTask(taskId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(taskId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}
