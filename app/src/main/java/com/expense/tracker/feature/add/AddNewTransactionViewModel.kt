package com.expense.tracker.feature.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.core.domain.models.incomeCategories
import com.expense.tracker.core.domain.repo.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddNewTransactionUiState(
    val id: Long = -1,
    val tabs: List<String> = listOf("Income", "Expenses", "Transaction"),
    val selectedTabIndex: Int = 1,
    val showNumpad: Boolean = false,
    val categories: List<Category> = expenseCategories.values.toList(),
    val selectedCategory: Category = Category(),
    val amount: String = "0",
    val note: String = "",
    val currency: String = "₹"
)

@HiltViewModel
class AddNewTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(AddNewTransactionUiState())
        private set

    init {
        viewModelScope.launch {
            savedStateHandle.get<Long>("id")?.let { id ->
                if (id >= 0) {
                    transactionRepository.getTransactionById(id).collectLatest { transaction ->
                        if (transaction != null) {
                            uiState = uiState.copy(
                                id = transaction.id,
                                selectedTabIndex = if (transaction.type == TransactionType.INCOME) 0 else 1,
                                selectedCategory = incomeCategories[transaction.categoryId]
                                    ?: expenseCategories[transaction.categoryId] ?: Category(),
                                amount = "%.2f".format(transaction.amount),
                                note = transaction.note ?: "",
                                currency = transaction.currency,
                                showNumpad = true
                            )
                        }
                    }
                }
            }
        }
    }
    fun onTabSelected(index: Int) {
        val newCategories = when(index) {
            0 -> incomeCategories.values.toList()
            1 -> expenseCategories.values.toList()
            else -> emptyList()
        }
        uiState = uiState.copy(
            selectedTabIndex = index,
            categories = newCategories,
            selectedCategory = Category(),
            showNumpad = false,
            amount = "0"
        )
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
            var transaction = TransactionEntity(
                title = uiState.selectedCategory.label,
                amount = uiState.amount.toDouble(),
                type = if (uiState.selectedTabIndex == 0) TransactionType.INCOME else TransactionType.EXPENSE,
                categoryId = uiState.selectedCategory.id,
                note = uiState.note,
                currency = uiState.currency
            )
            if (uiState.id != -1L) transaction = transaction.copy(id = uiState.id)
            transactionRepository.addTransaction(transaction)
            uiState = uiState.copy(
                showNumpad = false,
                selectedCategory = Category(),
                amount = "0",
                note = ""
            )
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