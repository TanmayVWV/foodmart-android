package com.example.foodmart_android.ui.food

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodmart_android.data.FoodRepository
import com.example.foodmart_android.data.model.FoodCategory
import com.example.foodmart_android.data.model.FoodItem
import com.example.foodmart_android.data.remote.RetrofitInstance
import kotlinx.coroutines.launch


data class FoodUiState(
    val isLoading: Boolean = false,
    val allItems: List<FoodItem> = emptyList(),
    val displayedItems: List<FoodItem> = emptyList(),
    val categories: List<FoodCategory> = emptyList(),
    val selectedCategoryUuids: Set<String> = emptySet(),
    val error: String? = null

    )
class FoodViewModel : ViewModel(){
    private val repository = FoodRepository(
        RetrofitInstance.api
    )
    var uiState by mutableStateOf(FoodUiState())
        private set
    init{
        load()
    }
    fun load(){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try{
                val items = repository.getFoodItems()
                val cat = repository.getCategories()
                uiState = uiState.copy(
                    isLoading = false,
                    allItems = items,
                    displayedItems = items,
                    categories = cat,
                    selectedCategoryUuids = emptySet()
                    
                )

            }catch(e: Exception){
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Something went wrong......"
                )

            }

        }
    }
    fun onCategoryToggled(categoryUuid: String?){
        if (categoryUuid == null){
            uiState = uiState.copy(
                displayedItems = uiState.allItems,
                selectedCategoryUuids = emptySet()
            )
            return

        }
        val currentSelection = uiState.selectedCategoryUuids
        val newSelection = if(currentSelection.contains(categoryUuid)){
            currentSelection - categoryUuid
        }else{
            currentSelection + categoryUuid
        }
        val newDisplayed =
            if(newSelection.isEmpty()){
                uiState.allItems
            }else{
                uiState.allItems.filter { item ->
                    newSelection.contains(item.categoryUuid)
                }
            }
        uiState = uiState.copy(
            displayedItems = newDisplayed,
            selectedCategoryUuids = newSelection
        )




        }


    }

    

