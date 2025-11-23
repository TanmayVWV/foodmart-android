package com.example.foodmart_android.ui.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.foodmart_android.data.model.FoodCategory
import com.example.foodmart_android.data.model.FoodItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(viewModel: FoodViewModel = viewModel()) {
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food Mart") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            when {
                state.isLoading -> {
                    Text(
                        text = "Loading....",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error ?: "Unknown Error",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.load() }) {
                            Text(text = "Retry")
                        }
                    }
                }
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        CategoryRow(
                            categories = state.categories,
                            selectedCategoryUuid = state.selectedCategoryUuid,
                            onCategorySelected = { viewModel.onCategorySelected(it) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.displayedItems) { item ->
                                FoodItemCard(foodItem = item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryRow(
    categories: List<FoodCategory>,
    selectedCategoryUuid: String?,
    onCategorySelected: (String?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            FilterChip(
                selected = selectedCategoryUuid == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All") }
            )
        }
        items(categories) { category ->
            FilterChip(
                selected = selectedCategoryUuid == category.uuid,
                onClick = { onCategorySelected(category.uuid) },
                label = { Text(category.name) }
            )
        }
    }
}

@Composable
fun FoodItemCard(foodItem: FoodItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = foodItem.imageUrl,
                contentDescription = foodItem.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "$${foodItem.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = foodItem.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
        }
    }
}
