package com.expense.tracker.feature.recurring

data class RecurringPaymentUi(
    val id: Long,
    val title: String,
    val amountText: String,
    val frequencyLabel: String,
    val isActive: Boolean
)
