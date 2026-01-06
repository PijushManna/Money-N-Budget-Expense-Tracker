package com.expense.tracker.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    HomeScreenContainer()
}

@Composable
fun HomeScreenContainer(transactions: List<TransactionsViewType> = emptyList(), headerColor: Color = MaterialTheme.colorScheme.primaryContainer, modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxSize()) {
        Overview(Modifier, MaterialTheme.colorScheme.primaryContainer)
        TransactionDetails(transactions)
    }
}

@Composable
fun Overview(modifier: Modifier = Modifier, buttonColor: Color){
    LazyRow(modifier = modifier) {
        stickyHeader {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
                Text("2026", style = MaterialTheme.typography.bodyMedium)
                Text("Jan", style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(color = buttonColor, shape = RoundedCornerShape(18.dp)).clickable{}.padding(horizontal = 12.dp))
            }
        }
        items(10){
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                Text("Expense", style = MaterialTheme.typography.bodyMedium)
                Text("604", modifier = Modifier.padding(top = 4.dp), style = MaterialTheme.typography.titleLarge)
            }
        }
    }
    HorizontalDivider(thickness = 0.5.dp)
}

@Composable
fun ColumnScope.TransactionDetails(transactions: List<TransactionsViewType>){
    LazyColumn(
        Modifier.fillMaxWidth().weight(1F)
    ) {
        items(transactions){
            when(it){
                is TransactionsViewType.Header -> {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp)) {
                        Text(it.date, style = MaterialTheme.typography.bodySmall)
                        Text(it.total, style = MaterialTheme.typography.bodySmall)
                    }
                    HorizontalDivider(thickness = 0.5.dp)
                }
                is TransactionsViewType.Transaction -> {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = it.icon, contentDescription = it.label)
                            Text(it.label, style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(it.amount, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

sealed class TransactionsViewType {
    data class Header(val date: String,val total: String) : TransactionsViewType()
    data class Transaction(val icon: ImageVector, val label: String, val amount: String) : TransactionsViewType()
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
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

    HomeScreenContainer(transactions = fakeTransactions)
}