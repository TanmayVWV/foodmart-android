package com.example.foodmart_android.ui.foodlist
import com.example.foodmart_android.domain.model.FoodItem
import com.example.foodmart_android.domain.model.FoodCategory

data class FoodUiState(
    val isLoading: Boolean = false,
    val items: List<FoodItem> = emptyList(),
    val categories: List<FoodCategory> = emptyList(),
    val activeCategoryIds: Set<Int> = emptySet(),
    val errorMessage: String? = null
)