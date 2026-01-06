package com.expense.tracker.feature.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChartsScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        AnimatedDonutChartPreview()
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(thickness = 0.5.dp)
        PreviewCategoryStats()
    }
}

@Preview
@Composable
fun ChartsScreenPreview() {
    ChartsScreen()
}