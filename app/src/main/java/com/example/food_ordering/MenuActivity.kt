package com.example.food_ordering

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val menuRecyclerView = findViewById<RecyclerView>(R.id.menuRecyclerView)
        menuRecyclerView.layoutManager = LinearLayoutManager(this)

        val menuItems = listOf(
            MenuItem("Margherita Pizza", "Classic pizza with tomatoes, mozzarella, and basil", 12.99, "https://www.simplyrecipes.com/thmb/KRw_r32s4gQeOX-d07NWY1OlOFk=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-Margherita-Pizza-LEAD-1-2-5612573581b8439884b7a7966324b869.jpg"),
            MenuItem("Cheeseburger", "Beef patty with cheese, lettuce, and tomato on a brioche bun", 8.99, "https://www.mcdonalds.com/is/image/content/dam/usa/nfl/nutrition/items/regular/desktop/t-mcdonalds-cheeseburger.jpg"),
            MenuItem("Caesar Salad", "Romaine lettuce, croutons, Parmesan cheese, and Caesar dressing", 7.49, "https://www.jessicagavin.com/wp-content/uploads/2019/07/caesar-salad-10-1200.jpg"),
            MenuItem("Spaghetti Carbonara", "Pasta with eggs, cheese, pancetta, and pepper", 11.50, "https://www.allrecipes.com/thmb/Vg2GXE_f0s02_p_R9b2J1t_w2vA=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/11973-spaghetti-carbonara-ii-DDMFS-4x3-0638-393b4bf9146949368d1163150b3e604d.jpg"),
            MenuItem("Chocolate Cake", "Rich and moist chocolate cake with a fudge frosting", 5.99, "https://www.livewellbakeoften.com/wp-content/uploads/2021/07/Chocolate-Sponge-Cake-8s.jpg")
        )

        val menuAdapter = MenuAdapter(menuItems)
        menuRecyclerView.adapter = menuAdapter
    }
}
