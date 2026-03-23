package com.example.food_ordering

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val menuRecyclerView = findViewById<RecyclerView>(R.id.menuRecyclerView)
        menuRecyclerView.layoutManager = LinearLayoutManager(this)

        menuAdapter = MenuAdapter(MenuData.items)
        menuRecyclerView.adapter = menuAdapter

        val viewCartButton = findViewById<Button>(R.id.viewCartButton)
        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list in case a new item was added
        menuAdapter.notifyDataSetChanged()
    }
}
