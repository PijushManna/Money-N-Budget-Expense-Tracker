package com.expense.tracker.feature.budget.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.tracker.core.domain.models.BudgetVsSpent

@Composable
fun BudgetVsSpentRow(data: BudgetVsSpent) {

    val animatedProgress by animateFloatAsState(
        targetValue = data.progress.coerceIn(0f, 1f),
        label = "spent_progress"
    )
    val isOverBudget = data.spent > data.budget

    Column {
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Spent: ₹${data.spent}")
            Text("Remaining: ₹${data.remaining}")
        }
        OverBudgetIndicator(isOver = isOverBudget)
    }
}
