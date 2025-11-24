package com.example.foodmart_android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// item model for API
@Serializable
data class FoodItem(
    @SerialName("uuid")val uuid: String,
    @SerialName("name")val name: String,
    @SerialName("price")val price: Double,
    @SerialName("category_uuid")val categoryUuid: String,
    @SerialName("image_url")val imageUrl: String
)