package com.example.food_ordering

import java.io.Serializable

data class MenuItem(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
) : Serializable
