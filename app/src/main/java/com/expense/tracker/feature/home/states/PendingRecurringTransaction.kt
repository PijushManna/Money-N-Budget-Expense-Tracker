package com.expense.tracker.feature.home.states

import com.expense.tracker.core.data.local.entities.TransactionType

data class PendingRecurringTransaction(
    val id: Long,
    val title: String,
    val amountText: String,
    val frequencyLabel: String,
    val type: TransactionType
)
