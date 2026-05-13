package com.expense.tracker.feature.home.usecase

import com.expense.tracker.core.data.local.entities.AccountWithCurrency
import com.expense.tracker.core.data.local.entities.CurrencyEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.data.local.entities.TransactionWithAccount
import com.expense.tracker.feature.home.states.OverviewUiState
import com.expense.tracker.utils.formatAmount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetOverviewUiStateUseCase @Inject constructor() {
    operator fun invoke(
        transactions: Flow<List<TransactionWithAccount>>,
        accounts: Flow<List<AccountWithCurrency>>,
        year: Int,
        month: Int,
        baseCurrency: CurrencyEntity
    ): Flow<OverviewUiState> {
        val months = listOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )

        return combine(transactions, accounts) { transactions, accounts ->
            OverviewUiState(
                selectedYear = year.toString(),
                selectedMonth = months[month],
                totalIncome = transactions.filter { it.transaction.type == TransactionType.INCOME }
                    .sumOf { it.transaction.amount * it.currency.conversionFactor }
                    .formatAmount(baseCurrency.symbol),
                totalExpense = transactions.filter { it.transaction.type == TransactionType.EXPENSE }
                    .sumOf { it.transaction.amount * it.currency.conversionFactor * -1 }
                    .formatAmount(baseCurrency.symbol),
                totalBalance = accounts.sumOf { it.account.balance * it.currency.conversionFactor }
                    .formatAmount(baseCurrency.symbol),
                totalBalanceCalculation = accounts.joinToString(" + ") {
                    "${
                        it.account.balance.formatAmount(it.currency.symbol)
                    }${if (it.currency.code != baseCurrency.code) " x " + it.currency.conversionFactor else ""}"
                })
        }
    }
}