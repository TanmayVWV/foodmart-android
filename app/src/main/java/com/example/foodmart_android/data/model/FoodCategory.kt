package com.example.foodmart_android.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// category model for API
@Serializable
data class FoodCategory(
    @SerialName("uuid")val uuid: String,
    @SerialName("name")val name: String
)