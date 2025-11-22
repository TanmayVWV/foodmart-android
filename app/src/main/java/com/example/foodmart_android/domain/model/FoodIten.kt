package com.example.foodmart_android.domain.model

data class FoodItem(
    val id: String,
    val name: String,
    val price: Double,
    val categoryId: Int,
    val imageUrl: String
)