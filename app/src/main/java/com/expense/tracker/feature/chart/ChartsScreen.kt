package com.expense.tracker.feature.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.CategoryStat
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.utils.formatAmount
import java.time.LocalDate

@Composable
fun ChartsScreen(
    navController: NavController,
    viewModel: ChartsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ChartsScreenContent(
        uiState = uiState,
        navController = navController,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ChartsScreenContent(
    uiState: ChartsUiState,
    navController: NavController,
    onEvent: (ChartsEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Header(
                config = HeaderConfig(
                    title = "Charts",
                    navigationIcon = null,
                    onNavigationClick = {},
                    actions = {
                        TransactionTypeSwitch(
                            selectedType = uiState.transactionType,
                            onTypeSelected = { onEvent(ChartsEvent.ChangeTransactionType(it)) }
                        )
                    }
                )
            )
        },
        bottomBar = {
            Footer(currentRoute = "charts") {
                navController.navigate(it)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                FilterTabs(
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = { onEvent(ChartsEvent.FilterBy(it)) }
                )
            }

            item {
                PeriodNavigator(
                    periodLabel = uiState.periodLabel.ifBlank { uiState.selectedFilter },
                    onPreviousClick = { onEvent(ChartsEvent.PreviousPeriod) },
                    onNextClick = { onEvent(ChartsEvent.NextPeriod) }
                )
            }

            item {
                ChartSummaryCard(
                    transactionType = uiState.transactionType,
                    totalAmount = uiState.totalAmount,
                    transactionCount = uiState.transactionCount
                )
            }

            when {
                uiState.isLoading -> {
                    item { LoadingChartState() }
                }

                uiState.error != null -> {
                    item { ChartMessage(text = uiState.error) }
                }

                uiState.categoryStats.isEmpty() -> {
                    item {
                        EmptyChartState(
                            transactionType = uiState.transactionType,
                            periodLabel = uiState.periodLabel.ifBlank { uiState.selectedFilter }
                        )
                    }
                }

                else -> {
                    item {
                        AnimatedDonutChart(
                            data = uiState.categoryStats.map { it.title to it.value },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp
                        )
                    }

                    item {
                        CategoryStats(stats = uiState.categoryStats)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionTypeSwitch(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(0.5.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
    ) {
        listOf(TransactionType.EXPENSE, TransactionType.INCOME).forEach { type ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (selectedType == type) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onTypeSelected(type) }
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = if (selectedType == type) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun FilterTabs(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("Weekly", "Monthly", "Yearly")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        filters.forEach { filter ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (selectedFilter == filter) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onFilterSelected(filter) }
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filter,
                    color = if (selectedFilter == filter) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun PeriodNavigator(
    periodLabel: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous period")
        }

        Text(
            text = periodLabel,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onNextClick) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next period")
        }
    }
}

@Composable
private fun ChartSummaryCard(
    transactionType: TransactionType,
    totalAmount: Double,
    transactionCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transactionType.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = totalAmount.formatAmount(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "$transactionCount transactions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
private fun LoadingChartState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyChartState(
    transactionType: TransactionType,
    periodLabel: String
) {
    ChartMessage(
        text = "No ${transactionType.name.lowercase()} transactions for $periodLabel"
    )
}

@Composable
private fun ChartMessage(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Insights,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChartsScreenPreview() {
    val uiState = ChartsUiState(
        selectedFilter = "Monthly",
        selectedDate = LocalDate.now(),
        transactionType = TransactionType.EXPENSE,
        periodLabel = "May 2026",
        totalAmount = 12500.0,
        transactionCount = 4,
        categoryStats = listOf(
            CategoryStat(
                title = "Food",
                value = 6000.0,
                icon = Icons.Outlined.Insights
            ),
            CategoryStat(
                title = "Shopping",
                value = 6500.0,
                icon = Icons.Outlined.Insights
            )
        )
    )

    ChartsScreenContent(
        uiState = uiState,
        navController = rememberNavController(),
        onEvent = {}
    )
}
