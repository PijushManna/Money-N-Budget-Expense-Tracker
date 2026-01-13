package com.expense.tracker.feature.budget

import com.expense.tracker.core.data.local.entities.BudgetEntity
import com.expense.tracker.core.data.local.entities.CategoryEntity
import com.expense.tracker.core.domain.models.BudgetSummary
import com.expense.tracker.core.domain.models.CategorySpend

data class BudgetUiState(
    val commonBudget: Double = 0.0,
    val budgetSummary: BudgetSummary = BudgetSummary(0.0, 0.0),
    val categoryBudgets: List<BudgetEntity> = emptyList(),
    val categorySpends: Map<Long, CategorySpend> = emptyMap(),
    val categories: List<CategoryEntity> = emptyList(),
    val isLoading: Boolean = false
)
