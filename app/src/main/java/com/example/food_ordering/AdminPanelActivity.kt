package com.example.food_ordering

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        dbHelper = DatabaseHelper(this)

        val addItemButton = findViewById<MaterialButton>(R.id.addItemButton)
        val ordersListView = findViewById<ListView>(R.id.ordersListView)
        val statsTv = findViewById<TextView>(R.id.statsTv)

        // Navigation to add new food items
        addItemButton.setOnClickListener {
            startActivity(Intent(this, AddMenuItemActivity::class.java))
        }

        // Initial load of orders
        loadOrders(ordersListView, statsTv)
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list and stats whenever returning to this screen
        val ordersListView = findViewById<ListView>(R.id.ordersListView)
        val statsTv = findViewById<TextView>(R.id.statsTv)
        if (ordersListView != null && statsTv != null) {
            loadOrders(ordersListView, statsTv)
        }
    }

    private fun loadOrders(listView: ListView, statsTv: TextView) {
        val cursor = dbHelper.getAllOrders()
        val ordersList = mutableListOf<String>()
        var totalRevenue = 0.0

        cursor?.use { c: Cursor -> // Explicitly specified type to fix inference error
            if (c.moveToFirst()) {
                do {
                    // Indices based on DatabaseHelper: 1:email, 2:details, 3:total, 4:status
                    val email = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL))
                    val details = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DETAILS))
                    val total = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL))
                    val status = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_STATUS))

                    ordersList.add("User: $email\nItems: $details\nTotal: KSh $total\nStatus: $status")
                    totalRevenue += total
                } while (c.moveToNext())
            }
        }

        // Displaying the list
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ordersList)
        listView.adapter = adapter

        // Updating statistics
        statsTv.text = "Total Orders: ${ordersList.size} | Total Revenue: KSh ${String.format("%.2f", totalRevenue)}"
    }
}
