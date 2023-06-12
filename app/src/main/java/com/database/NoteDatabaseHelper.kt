import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "note_database"
        private const val DATABASE_VERSION = 1

        // Table name
        private const val TABLE_NAME = "notes"

        // Table columns
        private const val COLUMN_ID = "noteID"
        private const val COLUMN_TITLE = "noteTitle"
        private const val COLUMN_TEXT = "noteText"
        private const val COLUMN_TYPE = "noteType"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the notes table
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_TEXT TEXT, " +
                "$COLUMN_TYPE TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun deleteNoteByTitle(noteTitle: String): Boolean {
        val db = writableDatabase
        val whereClause = "$COLUMN_TITLE = ?"
        val whereArgs = arrayOf(noteTitle)
        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
        return deletedRows > 0
    }
}
