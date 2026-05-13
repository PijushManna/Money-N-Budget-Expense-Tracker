package com.expense.tracker.feature.home.usecase

import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.data.local.entities.TransactionWithAccount
import com.expense.tracker.core.data.mapper.CategoryIconMapper
import com.expense.tracker.feature.home.TransactionsViewType
import com.expense.tracker.utils.formatAmount
import com.expense.tracker.utils.toLocalDate
import com.expense.tracker.utils.toUiDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTransactionsViewTypeUseCase @Inject constructor() {
    operator fun invoke(transactions: Flow<List<TransactionWithAccount>>): Flow<List<TransactionsViewType>>{
        return transactions.map {   transactions ->
            transactions.groupBy { it.transaction.timestamp.toLocalDate() }.flatMap { (date, items) ->

                val totalIncome = items.filter { it.transaction.type == TransactionType.INCOME }
                    .groupBy { it.account.currencyCode }.mapValues { (_, list) ->
                        list.sumOf { it.transaction.amount }.formatAmount(list.first().currency.symbol)
                    }.values.joinToString(" + ")


                val totalExpense = items.filter { it.transaction.type == TransactionType.EXPENSE }
                    .groupBy { it.account.currencyCode }.mapValues { (_, list) ->
                        list.sumOf { it.transaction.amount }
                            .formatAmount(list.first().currency.symbol)
                    }.values.joinToString(" + ")


                val header = TransactionsViewType.Header(
                    date = date.toUiDate(), expense = totalExpense, income = totalIncome
                )

                val rows = items.map {
                    TransactionsViewType.Transaction(
                        icon = CategoryIconMapper.getCategoryIconFromName(it.transaction.categoryName),
                        label = it.transaction.title,
                        amount = "${if (it.transaction.type == TransactionType.INCOME) "+" else "-"} ${it.amount}",
                        categoryName = it.transaction.categoryName + " / " + it.account.name,
                        id = it.transaction.id
                    )
                }

                listOf(header) + rows
            }
        }
    }
}