package com.example.food_ordering

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        dbHelper = DatabaseHelper(this)

        val menuRecyclerView = findViewById<RecyclerView>(R.id.menuRecyclerView)
        val viewCartButton = findViewById<MaterialButton>(R.id.viewCartButton)
        val logoutButton = findViewById<ImageView>(R.id.logoutButton)

        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        
        loadMenu()

        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            // Clear session and go back to Login
            SessionManager.userEmail = null
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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
