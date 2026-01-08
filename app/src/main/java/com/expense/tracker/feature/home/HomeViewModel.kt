package com.expense.tracker.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.domain.repo.AccountRepository
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.feature.home.states.OverviewUiState
import com.expense.tracker.feature.home.states.PendingRecurringTransaction
import com.expense.tracker.utils.formatAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val recurringPaymentRepository: RecurringPaymentRepository,
    accountRepository: AccountRepository,
) : ViewModel() {
    private val currency: String = "₹"
    val overviewUiState = combine(
        transactionRepository.getTotalIncome(),
        transactionRepository.getTotalExpense(),
        accountRepository.getLisOfBalance(),
    ) { income, expense, balance ->
        val totalBalance = balance.sum()
        OverviewUiState(
            "2026",
            "Jan",
            income.formatAmount(currency),
            (-expense).formatAmount(currency),
            (totalBalance - (expense - income)).formatAmount(currency),
            formatProfitAndBalance(balance, expense, income, currency)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), OverviewUiState())

    val transactions = transactionRepository.getAllTransactions()
        .map { transactions -> TransactionsUiMapper.map(transactions) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val pendingTransactions = recurringPaymentRepository.getActiveRecurringPayments().map {
        it.map { it ->
            PendingRecurringTransaction(
                id = it.id,
                title = it.title,
                amountText = it.amount.formatAmount(currency),
                frequencyLabel = it.frequency.name.lowercase()
                    .replaceFirstChar { char -> char.uppercase() },
                type = it.type
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun verifyRecurringPayment(
        rpId: Long, accept: Boolean
    ) {
        viewModelScope.launch {

            val recurringPayment =
                recurringPaymentRepository.getRecurringPaymentById(rpId) ?: return@launch

            if (accept) {
                transactionRepository.addTransaction(
                    TransactionEntity(
                        title = recurringPayment.title,
                        amount = recurringPayment.amount,
                        type = recurringPayment.type,
                        categoryId = recurringPayment.cid,
                        timestamp = System.currentTimeMillis(),
                        note = "Verified from recurring payment"
                    )
                )
            }
            recurringPaymentRepository.updateRecurringPayment(recurringPayment.copy(nextHandlingDate = recurringPayment.calculateNextHandlingDate()))
        }
    }


    private fun formatProfitAndBalance(
        balances: List<Double>, expense: Double, income: Double, currency: String
    ): String {
        val profit = income - expense

        val formattedProfit = profit.formatAmount(currency)
        val formattedBalances = balances.joinToString(" + ") {
            it.formatAmount(currency)
        }

        return "Profit ($formattedProfit) • Balance ($formattedBalances)"
    }

}
