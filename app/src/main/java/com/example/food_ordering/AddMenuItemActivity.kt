package com.example.food_ordering

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddMenuItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu_item)

        dbHelper = DatabaseHelper(this)

        val itemNameEt = findViewById<TextInputEditText>(R.id.itemName)
        val itemDescEt = findViewById<TextInputEditText>(R.id.itemDescription)
        val itemPriceEt = findViewById<TextInputEditText>(R.id.itemPrice)
        val itemImageUrlEt = findViewById<TextInputEditText>(R.id.itemImageUrl)
        val saveItemButton = findViewById<MaterialButton>(R.id.saveItemButton)

        saveItemButton.setOnClickListener {
            val name = itemNameEt.text.toString()
            val description = itemDescEt.text.toString()
            val price = itemPriceEt.text.toString().toDoubleOrNull()
            val imageUrl = itemImageUrlEt.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && price != null && imageUrl.isNotEmpty()) {
                val newItem = MenuItem(name, description, price, imageUrl)
                
                val success = dbHelper.addMenuItem(newItem)

                if (success) {
                    Toast.makeText(this, "Item added successfully to Database", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
