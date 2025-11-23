package com.example.foodmart_android.data.remote
import com.example.foodmart_android.data.model.FoodItem
import com.example.foodmart_android.data.model.FoodCategory
import retrofit2.http.GET

interface FoodApi{
    @GET("food_items.json")
    suspend fun getFoodItems(): List<FoodItem>
    @GET("food_item_categories.json")
    suspend fun getFoodCategories(): List<FoodCategory>
}
