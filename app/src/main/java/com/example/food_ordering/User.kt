package com.example.food_ordering

data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)
