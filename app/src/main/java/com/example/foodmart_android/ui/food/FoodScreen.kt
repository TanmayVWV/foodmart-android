package com.example.foodmart_android.ui.food

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.foodmart_android.data.model.FoodCategory
import com.example.foodmart_android.data.model.FoodItem

@Composable
fun FoodScreen(viewModel: FoodViewModel = viewModel()) {
    val state = viewModel.uiState
    var showFilter by remember { mutableStateOf(false) }

    if (showFilter) {
        FilterSheet(
            categories = state.categories,
            selectedIds = state.selectedCategoryUuids,
            onToggle = viewModel::onCategoryToggled,
            onDismiss = { showFilter = false }
        )
    }

    Scaffold(
        topBar = { FoodTopBar { showFilter = true } }
    ) { padding ->
        FoodContent(
            state = state,
            onRetry = viewModel::load,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun FoodContent(
    state: FoodUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        when {
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(message = state.error, onRetry = onRetry)
            else -> FoodGrid(items = state.displayedItems)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodTopBar(onFilterClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text("Food Mart") },
        actions = {
            TextButton(onClick = onFilterClick) {
                Text("Filter")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    categories: List<FoodCategory>,
    selectedIds: Set<String>,
    onToggle: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Text(
                    text = "Filter Categories",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(categories) { category ->
                val isSelected = selectedIds.contains(category.uuid)
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggle(category.uuid) }
                        .padding(vertical = 12.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply")
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun FoodGrid(items: List<FoodItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item -> FoodItemCard(item) }
    }
}

@Composable
fun FoodItemCard(foodItem: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = foodItem.imageUrl,
                contentDescription = foodItem.name,
                modifier = Modifier.fillMaxWidth().weight(1f),
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
