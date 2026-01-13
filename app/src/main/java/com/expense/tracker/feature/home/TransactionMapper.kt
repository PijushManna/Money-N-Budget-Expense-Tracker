package com.expense.tracker.feature.home

import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.data.mapper.CategoryIconMapper
import com.expense.tracker.utils.formatAmount
import com.expense.tracker.utils.toLocalDate
import com.expense.tracker.utils.toUiDate

fun TransactionEntity.toTransactionViewType(): TransactionsViewType.Transaction {
    val isIncome = type == TransactionType.INCOME

    return TransactionsViewType.Transaction(
        icon = CategoryIconMapper.getCategoryIconFromName(categoryName),
        label = categoryName,
        amount = "${if (isIncome) "+" else "-"} ${amount.formatAmount(currency)}",
        id = id
    )
}


object TransactionsUiMapper {

    fun map(
        transactions: List<TransactionEntity>
    ): List<TransactionsViewType> {

        return transactions
            .sortedByDescending { it.timestamp }
            .groupBy { it.timestamp.toLocalDate() }
            .flatMap { (date, items) ->

                val totalIncome = items
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }

                val totalExpense = items
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }
                
                val currency = items.firstOrNull()?.currency ?: "₹"

                val header = TransactionsViewType.Header(
                    date = date.toUiDate(),
                    total = "Expenses: ${(-totalExpense).formatAmount(currency)}  •  Income: ${totalIncome.formatAmount(currency)}"
                )

                val rows = items.map { it.toTransactionViewType() }

                listOf(header) + rows
            }
    }
}

