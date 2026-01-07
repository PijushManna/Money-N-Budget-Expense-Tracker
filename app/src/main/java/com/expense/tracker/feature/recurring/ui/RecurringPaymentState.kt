package com.expense.tracker.feature.recurring.ui

import com.expense.tracker.core.data.local.entities.RecurringFrequency
import com.expense.tracker.core.data.local.entities.TransactionType

data class RecurringPaymentState(
    val title: String = "",
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val frequency: RecurringFrequency = RecurringFrequency.MONTHLY,
    val startDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)
