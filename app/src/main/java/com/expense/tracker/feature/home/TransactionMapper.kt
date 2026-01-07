package com.expense.tracker.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.ui.graphics.vector.ImageVector
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.core.domain.models.incomeCategories
import com.expense.tracker.utils.formatAmount
import com.expense.tracker.utils.toLocalDate
import com.expense.tracker.utils.toUiDate

fun TransactionEntity.toTransactionViewType(): TransactionsViewType.Transaction {
    val isIncome = type == TransactionType.INCOME

    return TransactionsViewType.Transaction(
        icon = categoryIconMapper(categoryId, type),
        label = categoryLabelMapper(categoryId, type),
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

fun categoryIconMapper(
    categoryId: Long,
    type: TransactionType
): ImageVector {
    return when(type){
        TransactionType.INCOME -> incomeCategories[categoryId]?.icon ?: Icons.Default.Receipt
        TransactionType.EXPENSE -> expenseCategories[categoryId]?.icon ?: Icons.Default.Receipt
    }
}

fun categoryLabelMapper(categoryId: Long, type: TransactionType): String {
    return when(type){
        TransactionType.INCOME -> incomeCategories[categoryId]?.label ?: "Unknown"
        TransactionType.EXPENSE -> expenseCategories[categoryId]?.label ?: "Unknown"
    }
}
