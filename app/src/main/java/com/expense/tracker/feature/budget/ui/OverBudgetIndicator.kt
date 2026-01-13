package com.expense.tracker.feature.budget.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun OverBudgetIndicator(isOver: Boolean) {

    val alpha by animateFloatAsState(
        targetValue = if (isOver) 1f else 0f,
        label = "over_budget_alpha"
    )

    if (alpha > 0f) {
        Text(
            text = "Over Budget",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
