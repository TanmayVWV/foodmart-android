package com.example.foodmart_android.tests

import com.example.foodmart_android.data.FoodRepository
import com.example.foodmart_android.data.model.FoodCategory
import com.example.foodmart_android.data.model.FoodItem

class FakeFoodRepository(
    private val items: List<FoodItem> = emptyList(),
    private val categories: List<FoodCategory> = emptyList(),
    private val shouldThrow: Boolean = false
) : FoodRepository {

    override suspend fun getFoodItems(): List<FoodItem> {
        if (shouldThrow) throw RuntimeException("Network Error")
        return items
    }

    override suspend fun getCategories(): List<FoodCategory> {
        if (shouldThrow) throw RuntimeException("Network Error")
        return categories
    }
}
