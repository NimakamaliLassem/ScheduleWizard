package com.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "user.db"
        private const val DATABASE_VERSION = 1

        // User table
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "userId"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SURNAME = "surname"
        private const val COLUMN_SEMESTER = "semester"
        private const val COLUMN_CLASS = "class"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_USERS " +
                "($COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_SURNAME TEXT, " +
                "$COLUMN_SEMESTER TEXT, " +
                "$COLUMN_CLASS TEXT);"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(name: String, surname: String, semester: String, className: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SURNAME, surname)
        values.put(COLUMN_SEMESTER, semester)
        values.put(COLUMN_CLASS, className)

        val userId = db.insert(TABLE_USERS, null, values)
        //db.close()
        return userId
    }

    // Add other CRUD methods as per your requirements
}
