package com.expense.tracker.feature.home

import androidx.compose.ui.graphics.vector.ImageVector

sealed class TransactionsViewType {
    data class Header(val date: String, val total: String) : TransactionsViewType()
    data class Transaction(
        val icon: ImageVector,
        val label: String,
        val amount: String
    ) : TransactionsViewType()
}