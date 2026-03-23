package com.example.food_ordering

object MenuData {
    val items = mutableListOf<MenuItem>()

    fun addItem(item: MenuItem) {
        items.add(item)
    }
}
