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
            transactionRepository.getTotalExpense()
        ) { transactions, income, expense ->
            val transactionViewTypes = TransactionsUiMapper.map(transactions)
            val balance = income - expense

            _uiState.update { currentState ->
                currentState.copy(
                    transactions = transactionViewTypes,
                    overview = currentState.overview.copy(
                        totalIncome = "₹${"%.2f".format(income)}",
                        totalExpense = "₹${"%.2f".format(expense)}",
                        totalBalance = "₹${"%.2f".format(balance)}"
                    )
                )
            }
        }.launchIn(viewModelScope)
    }
}