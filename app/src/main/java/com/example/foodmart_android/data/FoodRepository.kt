package com.example.foodmart_android.data

import com.example.foodmart_android.data.model.FoodItem
import com.example.foodmart_android.data.model.FoodCategory
import com.example.foodmart_android.data.remote.FoodApi

class FoodRepository(
    private val api: FoodApi
) {

    suspend fun getFoodItems(): List<FoodItem> =
        api.getFoodItems()

    suspend fun getCategories(): List<FoodCategory> =
        api.getFoodCategories()
}