package com.expense.tracker.core.domain.models

data class BudgetVsSpent(
    val budget: Double,
    val spent: Double
) {
    val remaining: Double
        get() = budget - spent

    val progress: Float
        get() = if (budget == 0.0) 0f else (spent / budget).toFloat()
}
