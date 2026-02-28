package com.example.food_ordering

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddMenuItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu_item)

        val itemNameEditText = findViewById<EditText>(R.id.itemNameEditText)
        val itemDescriptionEditText = findViewById<EditText>(R.id.itemDescriptionEditText)
        val itemPriceEditText = findViewById<EditText>(R.id.itemPriceEditText)
        val itemImageUrlEditText = findViewById<EditText>(R.id.itemImageUrlEditText)
        val saveItemButton = findViewById<Button>(R.id.saveItemButton)

        saveItemButton.setOnClickListener {
            val name = itemNameEditText.text.toString()
            val description = itemDescriptionEditText.text.toString()
            val price = itemPriceEditText.text.toString().toDoubleOrNull()
            val imageUrl = itemImageUrlEditText.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && price != null && imageUrl.isNotEmpty()) {
                // In a real app, you'd save this to a database or a remote server.
                // For now, we'll just add it to our in-memory list.
                val newMenuItem = MenuItem(name, description, price, imageUrl)
                // This is a temporary solution. Ideally, you would have a shared data source.
                val menuActivity = MenuActivity()
                menuActivity.addMenuItem(newMenuItem)

                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
