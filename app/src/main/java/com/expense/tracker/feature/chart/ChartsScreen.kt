package com.expense.tracker.feature.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig

@Composable
fun ChartsScreen(navController: NavController) {
    Scaffold(topBar = {
        Header(
            config = HeaderConfig(
                title = "Charts",
                navigationIcon = null,
                onNavigationClick = {}
            )
        )
    }, bottomBar = {
        Footer(currentRoute = "charts") {
            navController.navigate(it)
        }
    }) {
        Column(modifier = Modifier.fillMaxWidth().padding(it)) {
            AnimatedDonutChartPreview()
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp)
            PreviewCategoryStats()
        }
    }
}

@Preview
@Composable
fun ChartsScreenPreview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedDonutChartPreview()
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(thickness = 0.5.dp)
        PreviewCategoryStats()
    }
}