package com.example.food_ordering

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminPanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        val addItemButton = findViewById<Button>(R.id.addItemButton)
        val updateItemButton = findViewById<Button>(R.id.updateItemButton)
        val removeItemButton = findViewById<Button>(R.id.removeItemButton)

        addItemButton.setOnClickListener {
            Toast.makeText(this, "Add Item functionality to be implemented", Toast.LENGTH_SHORT).show()
        }

        updateItemButton.setOnClickListener {
            Toast.makeText(this, "Update Item functionality to be implemented", Toast.LENGTH_SHORT).show()
        }

        removeItemButton.setOnClickListener {
            Toast.makeText(this, "Remove Item functionality to be implemented", Toast.LENGTH_SHORT).show()
        }
    }
}
