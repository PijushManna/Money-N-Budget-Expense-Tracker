package com.expense.tracker.feature.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.repo.CategoryRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class BudgetViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                transactionRepository.getAllTransactions(),
                categoryRepository.getCategories(),
                transactionRepository.getTotalIncome(),
                transactionRepository.getTotalExpense()
            ) { transactions, categories, income, expense ->
                BudgetUiState(
                    transactions = transactions,
                    categories = categories,
                    totalIncome = income,
                    totalExpense = expense
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun addTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        categoryId: Long,
        note: String?
    ) {
        viewModelScope.launch {
            transactionRepository.addTransaction(
                TransactionEntity(
                    title = title,
                    amount = amount,
                    type = type,
                    categoryId = categoryId,
                    note = note
                )
            )
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }
    }
}
