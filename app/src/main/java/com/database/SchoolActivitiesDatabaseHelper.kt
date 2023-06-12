import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SchoolActivitiesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "activities.db"
        private const val DATABASE_VERSION = 1

        // School Activities table
        const val TABLE_SCHOOL_ACTIVITIES = "school_activities"
        private const val COLUMN_TASK_ID = "taskId"
        const val COLUMN_NAME = "name"
        private const val COLUMN_DEADLINE = "deadline"
        private const val COLUMN_TYPE = "type"
        const val COLUMN_USER_ID = "userId" // Foreign key column
        private const val TABLE_USERS = "users"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_SCHOOL_ACTIVITIES " +
                "($COLUMN_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DEADLINE TEXT, " +
                "$COLUMN_TYPE TEXT, " +
                "$COLUMN_USER_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID));" // Foreign key constraint
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SCHOOL_ACTIVITIES")
        onCreate(db)
    }

    fun addActivity(name: String, deadline: String, type: String, userId: Long): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_DEADLINE, deadline)
        values.put(COLUMN_TYPE, type)
        values.put(COLUMN_USER_ID, userId)

        val taskId = db.insert(TABLE_SCHOOL_ACTIVITIES, null, values)
        db.close()
        return taskId
    }

    fun deleteActivity(taskId: Long): Int {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_TASK_ID = ?"
        val whereArgs = arrayOf(taskId.toString())

        val rowsDeleted = db.delete(TABLE_SCHOOL_ACTIVITIES, whereClause, whereArgs)
        db.close()
        return rowsDeleted
    }
}
