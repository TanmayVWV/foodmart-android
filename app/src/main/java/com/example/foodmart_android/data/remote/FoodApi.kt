package com.example.foodmart_android.data.remote
import com.example.foodmart_android.data.remote.dto.FoodItemDto
import com.example.foodmart_android.data.remote.dto.FoodCategoryDto
import retrofit2.http.GET

interface FoodApi{
    @GET("food-items.json")
    suspend fun getFoodItems(): List<FoodItemDto>
    @GET("food-categories.json")
    suspend fun getFoodCategories(): List<FoodCategoryDto>
}
