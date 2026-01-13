package com.expense.tracker.feature.budget.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BudgetPieChart(
    items: List<CategoryChartItem>,
    modifier: Modifier = Modifier
) {
    val total = items.sumOf { it.value }

    Canvas(
        modifier = modifier.padding(16.dp)
    ) {
        var startAngle = -90f

        items.forEach { item ->
            val sweep = if (total == 0.0) 0f
            else (item.value / total * 360f).toFloat()

            drawArc(
                color = item.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true
            )

            startAngle += sweep
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetPieChartPreview() {
    MaterialTheme {
        BudgetPieChart(
            items = listOf(
                CategoryChartItem("Food", 3000.0, MaterialTheme.colorScheme.primary),
                CategoryChartItem("Transport", 2000.0, MaterialTheme.colorScheme.secondary),
                CategoryChartItem("Shopping", 1000.0, MaterialTheme.colorScheme.tertiary)
            ),
            modifier = Modifier.size(100.dp)
        )
    }
}
