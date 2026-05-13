package com.expense.tracker.feature.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.db.BudgetDatabase
import com.expense.tracker.core.data.local.entities.CurrencyEntity
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.test.DummyDataSeeder
import com.expense.tracker.core.domain.repo.AccountRepository
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.feature.home.states.DateFilter
import com.expense.tracker.feature.home.states.OverviewUiState
import com.expense.tracker.feature.home.states.PendingRecurringTransaction
import com.expense.tracker.feature.home.usecase.GetOverviewUiStateUseCase
import com.expense.tracker.feature.home.usecase.GetTransactionsViewTypeUseCase
import com.expense.tracker.utils.formatAmount
import com.expense.tracker.utils.getMonthRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val recurringPaymentRepository: RecurringPaymentRepository,
    getOverviewUiStateUseCase: GetOverviewUiStateUseCase,
    getTransactionsViewTypeUseCase: GetTransactionsViewTypeUseCase,
    private val accountRepository: AccountRepository,
    private val db: BudgetDatabase
) : ViewModel() {


    var filterStr by mutableStateOf<DateFilter>(DateFilter.Last3Months)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (db.accountDao().count() == 0) DummyDataSeeder(
                db.currencyDao(), db.accountDao(), db.transactionDao()
            ).seed()

            Log.d(
                "HomeViewModel", db.accountDao().getAllAccounts().toString()
            )

        }

    }

    val year = 2026
    val month = 3
    val ran = getMonthRange(year, month)
    val transactions = transactionRepository.getTransactionsBetween(ran.first, ran.second)
    val accounts = accountRepository.getAllAccounts()
    val baseCurrency = CurrencyEntity("INR", "₹", 1.0)

    val overviewUiState = getOverviewUiStateUseCase(transactions, accounts, year, month, baseCurrency).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        OverviewUiState()
    )
    val transactionsUiState = getTransactionsViewTypeUseCase(transactions).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    val pendingTransactions = recurringPaymentRepository.getActiveRecurringPayments().map {
        it.map {
            PendingRecurringTransaction(
                id = it.id,
                title = it.title,
                amountText = it.amount.formatAmount(baseCurrency.symbol),
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
                        note = "Verified from recurring payment",
                        smsId = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
