package com.expense.tracker.feature.budget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expense.tracker.core.data.local.entities.BudgetEntity
import com.expense.tracker.core.data.local.entities.CategoryEntity
import com.expense.tracker.core.domain.models.BudgetSummary
import com.expense.tracker.core.domain.models.BudgetVsSpent
import com.expense.tracker.feature.budget.BudgetUiState

@Composable
fun BudgetScreen(
    uiState: BudgetUiState,
    onSetCategoryBudget: (Long, Double) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var selectedAmount by remember { mutableStateOf<Double?>(null) }

    Box {
        Column {

            Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer)) {
                BudgetPieChartPreview()
                CommonBudgetHeader(uiState)
            }

            Divider()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.categories) { category ->

                    val budget = uiState.categoryBudgets
                        .find { it.categoryId == category.id }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clickable {
                                selectedCategory = category
                                selectedAmount = budget?.limitAmount
                            }
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            BudgetVsSpentRow(
                                data = BudgetVsSpent(
                                    budget = budget?.limitAmount ?: 0.0,
                                    spent = 0.0 // hook real spent later
                                )
                            )
                        }
                    }
                }
            }
        }

        selectedCategory?.let { category ->
            CategoryBudgetBottomSheet(
                category = category,
                initialAmount = selectedAmount,
                onSave = {
                    onSetCategoryBudget(category.id, it)
                    selectedCategory = null
                },
                onDismiss = { selectedCategory = null }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetScreenPreview() {
    MaterialTheme {
        BudgetScreen(
            uiState = BudgetUiState(
                commonBudget = 10000.0,
                budgetSummary = BudgetSummary(
                    totalLimit = 10000.0,
                    allocatedAmount = 6000.0
                ),
                categories = listOf(
                    CategoryEntity(1, "Food", Icons.Default.Fastfood.toString(), color = 0xFF000000),
                    CategoryEntity(2, "Transport", "",0L),
                    CategoryEntity(3, "Shopping","",0L)
                ),
                categoryBudgets = listOf(
                    BudgetEntity(1, 1, 3000.0, "2026-01"),
                    BudgetEntity(2, 2, 2000.0, "2026-01")
                )
            ),
            onSetCategoryBudget = { _, _ -> }
        )
    }
}
