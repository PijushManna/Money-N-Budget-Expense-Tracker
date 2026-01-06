package com.expense.tracker.feature.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class AddNewTransactionUiState(
    val tabs: List<String> = listOf("Income", "Expenses", "Transaction"),
    val selectedTabIndex: Int = 1,
    val showNumpad: Boolean = false,
    val categories: List<Category> = expenseCategories,
    val selectedCategory: Category = Category(),
    val amount: String = "0",
    val note: String = ""
)

@HiltViewModel
class AddNewTransactionViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(AddNewTransactionUiState())
        private set

    fun onTabSelected(index: Int) {
        uiState = uiState.copy(selectedTabIndex = index)
    }

    fun onCategorySelected(category: Category) {
        uiState = uiState.copy(
            selectedCategory = category,
            showNumpad = category.label.isNotBlank()
        )
    }

    fun onKeyPress(key: String) {
        uiState = uiState.copy(amount = handleInput(uiState.amount, key))
    }

    fun onNoteChange(note: String) {
        uiState = uiState.copy(note = note)
    }

    private fun handleInput(current: String, key: String): String {
        return when (key) {
            "⌫" -> if (current.length > 1) current.dropLast(1) else "0"
            "✓" -> current // Here we will probably save the transaction
            "+", "-", "Today" -> current
            else -> if (current == "0") key else current + key
        }
    }
}