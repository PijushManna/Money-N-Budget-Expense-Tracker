package com.expense.tracker.feature.recurring.ui

import com.expense.tracker.core.data.local.entities.RecurringFrequency
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories

data class RecurringPaymentState(
    val title: String = "",
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedCategory: Category = expenseCategories[106] ?: expenseCategories.values.first(),
    val frequency: RecurringFrequency = RecurringFrequency.MONTHLY,
    val startDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val showCategorySheet: Boolean = false
)
