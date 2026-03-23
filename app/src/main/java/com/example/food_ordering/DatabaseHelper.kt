package com.example.food_ordering

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FoodOrdering.db"
        private const val DATABASE_VERSION = 2

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

        // Orders table
        const val TABLE_ORDERS = "orders"
        const val COLUMN_ORDER_ID = "order_id"
        const val COLUMN_USER_EMAIL = "user_email"
        const val COLUMN_ORDER_DETAILS = "details"
        const val COLUMN_ORDER_TOTAL = "total"
        const val COLUMN_ORDER_STATUS = "status"
        const val COLUMN_PAYMENT_METHOD = "payment_method"
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

        val createOrdersTable = ("CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_ORDER_DETAILS + " TEXT,"
                + COLUMN_ORDER_TOTAL + " REAL,"
                + COLUMN_ORDER_STATUS + " TEXT,"
                + COLUMN_PAYMENT_METHOD + " TEXT" + ")")
        db?.execSQL(createOrdersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val createOrdersTable = ("CREATE TABLE " + TABLE_ORDERS + "("
                    + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_EMAIL + " TEXT,"
                    + COLUMN_ORDER_DETAILS + " TEXT,"
                    + COLUMN_ORDER_TOTAL + " REAL,"
                    + COLUMN_ORDER_STATUS + " TEXT,"
                    + COLUMN_PAYMENT_METHOD + " TEXT" + ")")
            db?.execSQL(createOrdersTable)
        }
    }

    // User Methods
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

    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_ID), "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun getPhoneByEmail(email: String): String? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_PHONE), "$COLUMN_EMAIL = ?", arrayOf(email), null, null, null)
        var phone: String? = null
        if (cursor.moveToFirst()) {
            phone = cursor.getString(0)
        }
        cursor.close()
        return phone
    }

    fun updatePassword(email: String, newPass: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_PASSWORD, newPass) }
        val result = db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
        return result > 0
    }

    // Menu Methods
    fun addMenuItem(item: MenuItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MENU_NAME, item.name)
        values.put(COLUMN_MENU_PRICE, item.price)
        values.put(COLUMN_MENU_IMAGE, item.imageUrl)
        values.put(COLUMN_MENU_DESC, item.description)
        val success = db.insert(TABLE_MENU, null, values)
        db.close()
        return success != -1L
    }

    fun getAllMenuItems(): List<MenuItem> {
        val menuList = mutableListOf<MenuItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MENU", null)
        if (cursor.moveToFirst()) {
            do {
                menuList.add(MenuItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(4),
                    cursor.getDouble(2),
                    cursor.getString(3)
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return menuList
    }

    fun updateMenuItem(item: MenuItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MENU_NAME, item.name)
        values.put(COLUMN_MENU_PRICE, item.price)
        values.put(COLUMN_MENU_IMAGE, item.imageUrl)
        values.put(COLUMN_MENU_DESC, item.description)
        val result = db.update(TABLE_MENU, values, "$COLUMN_MENU_ID = ?", arrayOf(item.id.toString()))
        db.close()
        return result > 0
    }

    fun deleteMenuItem(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_MENU, "$COLUMN_MENU_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    // Order Methods
    fun addOrder(email: String, details: String, total: Double, method: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_EMAIL, email)
        values.put(COLUMN_ORDER_DETAILS, details)
        values.put(COLUMN_ORDER_TOTAL, total)
        values.put(COLUMN_ORDER_STATUS, "Paid")
        values.put(COLUMN_PAYMENT_METHOD, method)
        val success = db.insert(TABLE_ORDERS, null, values)
        db.close()
        return success != -1L
    }

    fun getAllOrders(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_ORDERS", null)
    }

    fun clearOrderHistory() {
        val db = this.writableDatabase
        db.delete(TABLE_ORDERS, null, null)
        db.close()
    }
}
