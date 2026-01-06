package com.expense.tracker.feature.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expense.tracker.core.models.expenseCategories
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme

@Composable
fun AddNewTransactionScreen(modifier: Modifier = Modifier) {
    AddNewTransactionScreenContainer(modifier)
}

@Composable
fun AddNewTransactionScreenContainer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Header(
            HeaderConfig(
                title = "Add Transactions",
                navigationIcon = Icons.Default.Close,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.EventRepeat, contentDescription = "Event Repeat")
                    }
                })
        )
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            TransactionTabs(
                tabs = listOf("Income", "Expenses", "Transaction"),
                pagerState = PagerState(0) { 3 })
        }
        CategoryGrid(expenseCategories)
        AddAmountScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun AddNewTransactionScreenPreview() {
    MoneyBudgetExpenseTrackerTheme {
        AddNewTransactionScreenContainer()
    }
}