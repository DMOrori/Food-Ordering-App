package com.example.food_ordering

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FoodOrdering.db"
        private const val DATABASE_VERSION = 1

        // User table
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE = "phone"

        // Menu table
        const val TABLE_MENU = "menu"
        const val COLUMN_MENU_ID = "menu_id"
        const val COLUMN_MENU_NAME = "name"
        const val COLUMN_MENU_PRICE = "price"
        const val COLUMN_MENU_IMAGE = "image_url"
        const val COLUMN_MENU_DESC = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_PHONE + " TEXT" + ")")
        db?.execSQL(createUsersTable)

        val createMenuTable = ("CREATE TABLE " + TABLE_MENU + "("
                + COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MENU_NAME + " TEXT,"
                + COLUMN_MENU_PRICE + " REAL,"
                + COLUMN_MENU_IMAGE + " TEXT,"
                + COLUMN_MENU_DESC + " TEXT" + ")")
        db?.execSQL(createMenuTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MENU")
        onCreate(db)
    }

    fun addUser(user: User): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, user.name)
        values.put(COLUMN_EMAIL, user.email)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_PHONE, user.phone)
        val success = db.insert(TABLE_USERS, null, values)
        db.close()
        return success != -1L
    }

    fun checkUser(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID)
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, pass)
        val cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        db.close()
        return count > 0
    }
}
