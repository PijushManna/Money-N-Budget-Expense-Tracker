package com.expense.tracker.feature.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.core.domain.repo.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddNewTransactionUiState(
    val tabs: List<String> = listOf("Income", "Expenses", "Transaction"),
    val selectedTabIndex: Int = 1,
    val showNumpad: Boolean = false,
    val categories: List<Category> = expenseCategories.values.toList(),
    val selectedCategory: Category = Category(),
    val amount: String = "0",
    val note: String = ""
)

@HiltViewModel
class AddNewTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

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
        when (key) {
            "✓" -> saveTransaction()
            else -> uiState = uiState.copy(amount = handleInput(uiState.amount, key))
        }
    }

    fun onNoteChange(note: String) {
        uiState = uiState.copy(note = note)
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            uiState = AddNewTransactionUiState()
            val transaction = TransactionEntity(
                title = uiState.selectedCategory.label,
                amount = uiState.amount.toDouble(),
                type = if (uiState.selectedTabIndex == 0) TransactionType.INCOME else TransactionType.EXPENSE,
                categoryId = uiState.selectedCategory.id,
                note = uiState.note
            )
            transactionRepository.addTransaction(transaction)
        }
    }

    private fun handleInput(current: String, key: String): String {
        return when (key) {
            "⌫" -> if (current.length > 1) current.dropLast(1) else "0"
            "+", "-", "Today" -> current
            else -> if (current == "0") key else current + key
        }
    }
}