
package com.expense.tracker.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.ui.graphics.vector.ImageVector
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.utils.toLocalDate
import com.expense.tracker.utils.toUiDate

fun TransactionEntity.toTransactionViewType(): TransactionsViewType.Transaction {
    val isIncome = type == TransactionType.INCOME

    return TransactionsViewType.Transaction(
        icon = categoryIconMapper(categoryId, type),
        label = categoryLabelMapper(categoryId, type),
        amount = buildString {
            append(if (isIncome) "+" else "-")
            append("₹")
            append(amount.toInt())
        }
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

                val header = TransactionsViewType.Header(
                    date = date.toUiDate(),
                    total = "Expenses: ₹${totalExpense.toInt()}  •  Income: ₹${totalIncome.toInt()}"
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
        TransactionType.EXPENSE -> expenseCategories[categoryId]?.icon ?: Icons.Default.Receipt
        else -> Icons.Default.Receipt
    }
}

fun categoryLabelMapper(categoryId: Long, type: TransactionType): String {
    return when(type){
        TransactionType.EXPENSE -> expenseCategories[categoryId]?.label ?: "Unknown"
        else -> "Unknown"
    }
}
