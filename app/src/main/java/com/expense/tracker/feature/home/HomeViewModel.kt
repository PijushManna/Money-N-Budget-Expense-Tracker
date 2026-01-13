package com.expense.tracker.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.repo.AccountRepository
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.core.domain.usecase.AddSmsTransactionsUseCase
import com.expense.tracker.feature.chart.use_case.GetTransactionsUseCase
import com.expense.tracker.feature.home.states.OverviewUiState
import com.expense.tracker.feature.home.states.PendingRecurringTransaction
import com.expense.tracker.utils.formatAmount
import com.expense.tracker.utils.getMonthRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val recurringPaymentRepository: RecurringPaymentRepository,
    private val accountRepository: AccountRepository,
    private val addSmsTransactionsUseCase: AddSmsTransactionsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            addSmsTransactionsUseCase()
        }
    }

    private val currency: String = "₹"
    val ran = getMonthRange(2026, 2)
    val transactions  = getTransactionsUseCase(
        ran.first,
        ran.second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    val overviewUiState = transactions.map { it ->
        OverviewUiState(
            selectedYear = "2026",
            selectedMonth = "March",
            totalIncome = it.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }.formatAmount(),
            totalExpense = it.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }.formatAmount(),
            totalBalance = "0",
            totalBalanceCalculation = ""
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), OverviewUiState())

    val pendingTransactions = recurringPaymentRepository.getActiveRecurringPayments().map {
        it.map {
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
