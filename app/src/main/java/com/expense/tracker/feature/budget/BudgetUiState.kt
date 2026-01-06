package com.expense.tracker.feature.budget

import com.expense.tracker.core.data.local.entities.CategoryEntity
import com.expense.tracker.core.data.local.entities.TransactionEntity

data class BudgetUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val isLoading: Boolean = false
)
