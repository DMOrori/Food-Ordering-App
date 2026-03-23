package com.example.food_ordering

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class AddMenuItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var editingItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu_item)

        dbHelper = DatabaseHelper(this)

        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        val itemNameLayout = findViewById<TextInputLayout>(R.id.itemNameLayout)
        val itemNameEt = findViewById<TextInputEditText>(R.id.itemName)
        val itemDescEt = findViewById<TextInputEditText>(R.id.itemDescription)
        val itemPriceEt = findViewById<TextInputEditText>(R.id.itemPrice)
        val itemImageUrlEt = findViewById<TextInputEditText>(R.id.itemImageUrl)
        val imagePreview = findViewById<ImageView>(R.id.imagePreview)
        val saveItemButton = findViewById<MaterialButton>(R.id.saveItemButton)

        // Check if we are in Edit Mode
        editingItem = intent.getSerializableExtra("EDIT_ITEM") as? MenuItem
        
        if (editingItem != null) {
            headerTitle.text = "Edit Menu Item"
            saveItemButton.text = "Update Item"
            
            itemNameEt.setText(editingItem?.name)
            itemDescEt.setText(editingItem?.description)
            itemPriceEt.setText(editingItem?.price.toString())
            itemImageUrlEt.setText(editingItem?.imageUrl)
            
            imagePreview.visibility = View.VISIBLE
            Glide.with(this).load(editingItem?.imageUrl).into(imagePreview)
        }

        itemNameLayout.setEndIconOnClickListener {
            val query = itemNameEt.text.toString().trim()
            if (query.isNotEmpty()) {
                searchFoodOnline(query, itemDescEt, itemImageUrlEt, imagePreview)
            } else {
                Toast.makeText(this, "Enter a food name first", Toast.LENGTH_SHORT).show()
            }
        }

        saveItemButton.setOnClickListener {
            val name = itemNameEt.text.toString()
            val description = itemDescEt.text.toString()
            val price = itemPriceEt.text.toString().toDoubleOrNull()
            val imageUrl = itemImageUrlEt.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && price != null && imageUrl.isNotEmpty()) {
                
                if (editingItem != null) {
                    // Update existing item
                    val updatedItem = MenuItem(editingItem!!.id, name, description, price, imageUrl)
                    if (dbHelper.updateMenuItem(updatedItem)) {
                        Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Add new item
                    val newItem = MenuItem(name = name, description = description, price = price, imageUrl = imageUrl)
                    if (dbHelper.addMenuItem(newItem)) {
                        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchFoodOnline(name: String, descEt: TextInputEditText, urlEt: TextInputEditText, preview: ImageView) {
        Toast.makeText(this, "Searching for '$name'...", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val encodedName = URLEncoder.encode(name, "UTF-8")
                val apiUrl = URL("https://www.themealdb.com/api/json/v1/1/search.php?s=$encodedName")
                val connection = apiUrl.openConnection() as HttpURLConnection
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(responseText)
                val mealsArray = jsonResponse.optJSONArray("meals")

                withContext(Dispatchers.Main) {
                    if (mealsArray != null && mealsArray.length() > 0) {
                        val meal = mealsArray.getJSONObject(0)
                        val foundName = meal.getString("strMeal")
                        val foundImage = meal.getString("strMealThumb")
                        val foundCategory = meal.getString("strCategory")
                        val foundArea = meal.getString("strArea")

                        descEt.setText("Delicious $foundCategory from $foundArea. $foundName prepared with fresh ingredients.")
                        urlEt.setText(foundImage)
                        preview.visibility = View.VISIBLE
                        Glide.with(this@AddMenuItemActivity).load(foundImage).into(preview)
                        Toast.makeText(this@AddMenuItemActivity, "Found: $foundName", Toast.LENGTH_SHORT).show()
                    } else {
                        val fallbackImageUrl = "https://source.unsplash.com/featured/?${encodedName},food"
                        urlEt.setText(fallbackImageUrl)
                        descEt.setText("Freshly prepared $name made to order.")
                        preview.visibility = View.VISIBLE
                        Glide.with(this@AddMenuItemActivity).load(fallbackImageUrl).into(preview)
                        Toast.makeText(this@AddMenuItemActivity, "Generated placeholder for '$name'", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddMenuItemActivity, "Error searching online.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
