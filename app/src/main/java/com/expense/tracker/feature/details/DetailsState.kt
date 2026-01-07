package com.expense.tracker.feature.details

import com.expense.tracker.core.data.local.entities.TransactionType

data class DetailsState(
    val id: Long = 0L,
    val title: String = "",
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val category: String = "",
    val date: String = "",
    val note: String = "",
    val currency: String = "₹"
)
