import SchoolActivitiesDatabaseHelper.Companion.COLUMN_NAME
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class ExtracurricularActivitiesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "extracurricular_activities.db"
        private const val DATABASE_VERSION = 1

        // Extracurricular Activities table
        const val TABLE_EXTRACURRICULAR_ACTIVITIES = "extracurricular_activities"
        private const val COLUMN_ACTIVITY_ID = "activityId"
        const val COLUMN_ACTIVITY_NAME = "activityName"
        private const val COLUMN_NOTES = "notes"
        const val COLUMN_USER_ID = "userId" // Foreign key column
        private const val TABLE_USERS = "users"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_EXTRACURRICULAR_ACTIVITIES " +
                "($COLUMN_ACTIVITY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ACTIVITY_NAME TEXT, " +
                "$COLUMN_NOTES TEXT, " +
                "$COLUMN_USER_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID));" // Foreign key constraint
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXTRACURRICULAR_ACTIVITIES")
        onCreate(db)
    }

    fun addActivity(activityName: String, notes: String, userId: Long): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ACTIVITY_NAME, activityName)
        values.put(COLUMN_NOTES, notes)
        values.put(COLUMN_USER_ID, userId)

        val activityId = db.insert(TABLE_EXTRACURRICULAR_ACTIVITIES, null, values)
        db.close()
        return activityId
    }


    fun getFirstTwoActivities(): List<String>? {
        val activities: MutableList<String> = ArrayList()
        val db = readableDatabase
        val query = "SELECT $COLUMN_ACTIVITY_NAME FROM $TABLE_EXTRACURRICULAR_ACTIVITIES LIMIT 2"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val activityName = cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY_NAME))
                activities.add(activityName)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return activities
    }
    // Add other CRUD methods as per your requirements


}
