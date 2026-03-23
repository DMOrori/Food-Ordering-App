package com.example.food_ordering

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        dbHelper = DatabaseHelper(this)

        val menuRecyclerView = findViewById<RecyclerView>(R.id.menuRecyclerView)
        val viewCartButton = findViewById<Button>(R.id.viewCartButton)

        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        
        loadMenu()

        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadMenu()
    }

    private fun loadMenu() {
        val menuItems = dbHelper.getAllMenuItems()
        adapter = MenuAdapter(menuItems)
        findViewById<RecyclerView>(R.id.menuRecyclerView).adapter = adapter
    }
}
