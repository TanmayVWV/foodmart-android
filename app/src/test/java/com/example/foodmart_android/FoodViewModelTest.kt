package com.example.foodmart_android

import com.example.foodmart_android.data.model.FoodCategory
import com.example.foodmart_android.data.model.FoodItem
import com.example.foodmart_android.tests.FakeFoodRepository
import com.example.foodmart_android.ui.food.FoodViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

//add simple fake repo for viewmodel tests
@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //initial test for FoodViewModel load success
    @Test
    fun `load success updates state correctly`() = runTest {
        val items = listOf(
            FoodItem("1", "Apple", 1.0, "produce", "url_1"),
            FoodItem("2", "Steak", 10.0, "meat", "url_2")
        )
        val categories = listOf(
            FoodCategory("produce", "Produce"),
            FoodCategory("meat", "Meat")
        )
        val repo = FakeFoodRepository(items = items, categories = categories)

        val viewModel = FoodViewModel(repo)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.allItems).isEqualTo(items)
        assertThat(state.categories).isEqualTo(categories)
        assertThat(state.displayedItems).isEqualTo(items)
    }

    //to test error state when repo throws
    @Test
    fun `load failure sets error state`() = runTest {
        val repo = FakeFoodRepository(shouldThrow = true)

        val viewModel = FoodViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNotNull()
        assertThat(state.allItems).isEmpty()
    }

    // test for filtering items by category
    @Test
    fun `filtering by category updates displayed items`() = runTest {

        val apple = FoodItem("1", "Apple", 1.0, "produce", "url_1")
        val steak = FoodItem("2", "Steak", 10.0, "meat", "url_2")
        val items = listOf(apple, steak)
        val categories = listOf(
            FoodCategory("produce", "Produce"),
            FoodCategory("meat", "Meat")
        )
        val repo = FakeFoodRepository(items = items, categories = categories)
        val viewModel = FoodViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onCategoryToggled("produce")

        val state = viewModel.uiState
        assertThat(state.selectedCategoryUuids).containsExactly("produce")
        assertThat(state.displayedItems).containsExactly(apple)
        assertThat(state.displayedItems).doesNotContain(steak)
    }

    // test to see if multiple category filters work
    @Test
    fun `multiple selections work correctly`() = runTest {

        val apple = FoodItem("1", "Apple", 1.0, "produce", "url_1")
        val steak = FoodItem("2", "Steak", 10.0, "meat", "url_2")
        val carrot = FoodItem("3", "Carrot", 0.5, "produce", "url_3")
        val bread = FoodItem("4", "Bread", 2.5, "grains", "url_4")

        val items = listOf(apple, steak, carrot, bread)
        val categories = listOf(
            FoodCategory("produce", "Produce"),
            FoodCategory("meat", "Meat"),
            FoodCategory("grains", "Grains")
        )
        val repo = FakeFoodRepository(items = items, categories = categories)
        val viewModel = FoodViewModel(repo)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onCategoryToggled("produce")
        viewModel.onCategoryToggled("meat")

        val state = viewModel.uiState
        assertThat(state.selectedCategoryUuids).containsExactly("produce", "meat")
        assertThat(state.displayedItems).containsExactly(apple, carrot, steak)
        assertThat(state.displayedItems).doesNotContain(bread)
    }
}
