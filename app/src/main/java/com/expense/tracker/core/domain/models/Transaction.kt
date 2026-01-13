package com.expense.tracker.core.domain.models

import com.expense.tracker.core.data.local.entities.TransactionType

data class Transaction(
    val amount: Double,
    val type: TransactionType,
    val merchant: String?,
    val sender: String,
    val date: Long,
    val rawSms: String
)