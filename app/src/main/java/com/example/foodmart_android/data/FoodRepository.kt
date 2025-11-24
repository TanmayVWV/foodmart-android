package com.example.foodmart_android.data

import com.example.foodmart_android.data.model.FoodCategory
import com.example.foodmart_android.data.model.FoodItem
import com.example.foodmart_android.data.remote.FoodApi

// repo for loading items and categories
interface FoodRepository {
    suspend fun getFoodItems(): List<FoodItem>
    suspend fun getCategories(): List<FoodCategory>
}

// implementation of repo
class FoodRepositoryImpl(
    private val api: FoodApi
) : FoodRepository {

    override suspend fun getFoodItems(): List<FoodItem> =
        api.getFoodItems()

    override suspend fun getCategories(): List<FoodCategory> =
        api.getFoodCategories()
}
