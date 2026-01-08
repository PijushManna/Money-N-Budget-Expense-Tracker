package com.expense.tracker.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.expense.tracker.R
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.feature.common.TripleActionDialog
import com.expense.tracker.feature.home.states.OverviewUiState
import com.expense.tracker.feature.home.states.PendingRecurringTransaction

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val overviewUiState by viewModel.overviewUiState.collectAsState()
    val overview by derivedStateOf { overviewUiState }
    val transactions by viewModel.transactions.collectAsState()
    val pendingTransactions by viewModel.pendingTransactions.collectAsState()
    var rpId by remember { mutableStateOf<Long?>(null) }
    var showRPDialog by remember { mutableStateOf(false) }

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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Overview(Modifier, uiState = overview)
            PendingTransactions(pendingTransactions){
                showRPDialog = true
                rpId = it.id
            }
            TransactionDetails(transactions , navController)
        }
    }

    TripleActionDialog(
        visible = showRPDialog,
        title = "Confirm Recurring Payment",
        message = "Did you complete this recurring payment?",

        confirmText = "Accept",
        onConfirm = {
            if (rpId != null) {
                viewModel.verifyRecurringPayment(rpId!!, true)
                showRPDialog = false
            }
                    },

        rejectText = "Reject",
        onReject = {
            if (rpId != null) {
                viewModel.verifyRecurringPayment(rpId!!, false)
                showRPDialog = false
            }
        },

        neutralText = "Later",
        onNeutral = {
            showRPDialog = false
        },

        onDismiss = { showRPDialog = false }
    )

}

@Composable
fun PendingTransactions(
    uiState: List<PendingRecurringTransaction>,
    onVerifyClick: (PendingRecurringTransaction) -> Unit
) {
    if (uiState.isNotEmpty()) {
        Column {
            Text(
                text = "Pending Recurring Transactions",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(uiState) { transaction ->
                    PendingRecurringTransactionRow(
                        amountText = transaction.amountText,
                        frequencyLabel = transaction.frequencyLabel,
                        title = transaction.title,
                    ){
                        onVerifyClick(transaction)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenContainer(
    transactions: List<TransactionsViewType> = emptyList(), modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TransactionDetails(transactions, navController = rememberNavController())
    }
}

@Composable
fun Overview(modifier: Modifier = Modifier, uiState: OverviewUiState) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        HorizontalDivider(thickness = 0.5.dp)
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(uiState.selectedYear, style = MaterialTheme.typography.bodyMedium)
                Text(
                    uiState.selectedMonth,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, MaterialTheme.colorScheme.tertiary,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {}
                        .padding(horizontal = 12.dp))
            }
            LazyRow(horizontalArrangement = Arrangement.SpaceAround) {
                item {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp)
                    ) {
                        Text("Expense", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            uiState.totalExpense,
                            modifier = Modifier.padding(top = 4.dp),
                            style = MaterialTheme.typography.titleMedium
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
                            style = MaterialTheme.typography.titleMedium
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
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        Text(
            uiState.totalBalanceCalculation,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            textAlign = TextAlign.End
        )
        HorizontalDivider(thickness = 0.5.dp)
    }
}

@Composable
fun ColumnScope.TransactionDetails(transactions: List<TransactionsViewType>, navController: NavController) {
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
                            .clickable {
                                navController.navigate("details/${it.id}")
                            }
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
    HomeScreenContainer()
}
