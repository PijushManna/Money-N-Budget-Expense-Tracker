package com.expense.tracker.feature.home.states

import com.expense.tracker.feature.home.TransactionsViewType

data class HomeUiState(
    val transactions: List<TransactionsViewType> = emptyList(),
    val pendingTransactions: List<PendingRecurringTransaction> = emptyList(),
    val overview: OverviewUiState = OverviewUiState()
)