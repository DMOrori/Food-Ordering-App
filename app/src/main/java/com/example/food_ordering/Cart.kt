package com.example.food_ordering

object Cart {
    val items = mutableListOf<MenuItem>()

    fun addItem(item: MenuItem) {
        items.add(item)
    }

    fun getCartTotal(): Double {
        return items.sumOf { it.price }
    }
}
