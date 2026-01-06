package com.expense.tracker.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TransactionsViewType {
    data class Header(val date: String, val total: String) : TransactionsViewType()
    data class Transaction(
        val icon: ImageVector,
        val label: String,
        val amount: String
    ) : TransactionsViewType()
}

val fakeTransactions = listOf(
    TransactionsViewType.Header(
        date = "5 Jan, Monday",
        total = "Expenses: ₹104  •  Income: ₹5,000"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.AccountBalance,
        label = "Salary",
        amount = "+₹5,000"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.LocalCafe,
        label = "Coffee",
        amount = "-₹80"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.DirectionsBus,
        label = "Transport",
        amount = "-₹24"
    ),

    TransactionsViewType.Header(
        date = "4 Jan, Sunday",
        total = "Expenses: ₹850  •  Income: ₹0"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.Restaurant,
        label = "Lunch",
        amount = "-₹350"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.ShoppingCart,
        label = "Groceries",
        amount = "-₹500"
    ),

    TransactionsViewType.Header(
        date = "3 Jan, Saturday",
        total = "Expenses: ₹1,200  •  Income: ₹2,000"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.Work,
        label = "Freelance Payment",
        amount = "+₹2,000"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.Movie,
        label = "Movie Tickets",
        amount = "-₹400"
    ),
    TransactionsViewType.Transaction(
        icon = Icons.Default.Fastfood,
        label = "Snacks",
        amount = "-₹800"
    )
)