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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.expense.tracker.R
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.feature.home.states.OverviewUiState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(modifier = modifier, topBar = {
        Header(
            config = HeaderConfig(
                title = stringResource(R.string.app_name),
                navigationIcon = null,
                onNavigationClick = {}
            )
        )
    }, bottomBar = {
        Footer(currentRoute = "home") {
            navController.navigate(it)
        }
    }) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(it)) {
            Overview(Modifier, uiState.overview)
            TransactionDetails(uiState.transactions)
        }
    }
}

@Composable
fun HomeScreenContainer(
    transactions: List<TransactionsViewType> = emptyList(), modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TransactionDetails(transactions)
    }
}

@Composable
fun Overview(modifier: Modifier = Modifier, uiState: OverviewUiState) {
    LazyRow(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        stickyHeader {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(uiState.selectedYear, style = MaterialTheme.typography.bodyMedium)
                Text(
                    uiState.selectedMonth,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {}
                        .padding(horizontal = 12.dp))
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp)
            ) {
                Text("Expense", style = MaterialTheme.typography.bodyMedium)
                Text(
                    uiState.totalExpense,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp)
            ) {
                Text("Income", style = MaterialTheme.typography.bodyMedium)
                Text(
                    uiState.totalIncome,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp)
            ) {
                Text("Balance", style = MaterialTheme.typography.bodyMedium)
                Text(
                    uiState.totalBalance,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
    HorizontalDivider(thickness = 0.5.dp)
}

@Composable
fun ColumnScope.TransactionDetails(transactions: List<TransactionsViewType>) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .weight(1F)
    ) {
        items(transactions) {
            when (it) {
                is TransactionsViewType.Header -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    ) {
                        Text(it.date, style = MaterialTheme.typography.bodySmall)
                        Text(it.total, style = MaterialTheme.typography.bodySmall)
                    }
                    HorizontalDivider(thickness = 0.5.dp)
                }

                is TransactionsViewType.Transaction -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}