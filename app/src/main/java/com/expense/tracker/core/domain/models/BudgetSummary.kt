package com.expense.tracker.core.domain.models

data class BudgetSummary(
    val totalLimit: Double,
    val allocatedAmount: Double
) {
    val remaining: Double
        get() = totalLimit - allocatedAmount

    val progress: Float
        get() = if (totalLimit == 0.0) 0f else (allocatedAmount / totalLimit).toFloat()
}
