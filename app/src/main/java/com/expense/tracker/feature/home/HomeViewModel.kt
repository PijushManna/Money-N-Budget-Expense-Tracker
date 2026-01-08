package com.expense.tracker.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.feature.home.states.HomeUiState
import com.expense.tracker.feature.home.states.PendingRecurringTransaction
import com.expense.tracker.utils.formatAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val recurringPaymentRepository: RecurringPaymentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(
            transactionRepository.getAllTransactions(),
            transactionRepository.getTotalIncome(),
            transactionRepository.getTotalExpense(),
            transactionRepository.getFirstTransaction(),
            recurringPaymentRepository.getActiveRecurringPayments()
        ) { transactions, income, expense, firstTransaction, recurringPayments ->
            val transactionViewTypes = TransactionsUiMapper.map(transactions)
            val balance = income - expense
            val currency = firstTransaction?.currency ?: "₹"

            _uiState.update { currentState ->
                currentState.copy(
                    transactions = transactionViewTypes,
                    pendingTransactions = recurringPayments.map { 
                        PendingRecurringTransaction(
                            id = it.id,
                            title = it.title,
                            amountText = it.amount.formatAmount(currency),
                            frequencyLabel = it.frequency.name.lowercase().replaceFirstChar { char -> char.uppercase() },
                            type = it.type
                        )
                    },
                    overview = currentState.overview.copy(
                        totalIncome = income.formatAmount(currency),
                        totalExpense = (-expense).formatAmount(currency),
                        totalBalance = balance.formatAmount(currency)
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun verifyRecurringPayment(transaction: PendingRecurringTransaction) {
        viewModelScope.launch {
            val recurringPayment = recurringPaymentRepository.getRecurringPaymentById(transaction.id)
            if (recurringPayment != null) {
                transactionRepository.addTransaction(
                    TransactionEntity(
                        title = recurringPayment.title,
                        amount = recurringPayment.amount,
                        type = recurringPayment.type,
                        categoryId = 27, // Default category
                        timestamp = System.currentTimeMillis(),
                        note = "Verified from recurring payment"
                    )
                )
                recurringPaymentRepository.deactivateRecurringPayment(transaction.id)
            }
        }
    }
}
