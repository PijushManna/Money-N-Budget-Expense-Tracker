package com.expense.tracker.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.feature.home.states.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    transactionRepository: TransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(
            transactionRepository.getAllTransactions(),
            transactionRepository.getTotalIncome(),
            transactionRepository.getTotalExpense(),
            transactionRepository.getFirstTransaction()
        ) { transactions, income, expense, firstTransaction ->
            val transactionViewTypes = TransactionsUiMapper.map(transactions)
            val balance = income - expense
            val currency = firstTransaction?.currency ?: "₹"

            _uiState.update { currentState ->
                currentState.copy(
                    transactions = transactionViewTypes,
                    overview = currentState.overview.copy(
                        totalIncome = "$currency${income.toInt()}",
                        totalExpense = "$currency${expense.toInt()}",
                        totalBalance = "$currency${balance.toInt()}"
                    )
                )
            }
        }.launchIn(viewModelScope)
    }
}
