package com.expense.tracker.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.expense.tracker.R
import com.expense.tracker.feature.add.AddNewTransactionScreenPreview
import com.expense.tracker.feature.chart.ChartsScreenPreview
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.feature.common.rememberHeaderConfig
import com.expense.tracker.feature.home.HomeScreenPreview
import com.expense.tracker.feature.profile.ProfileScreenPreview
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme

@Composable
fun MainContainer(modifier: Modifier = Modifier) {
    var nav by remember { mutableStateOf("home") }
    val headerConfig by rememberHeaderConfig(HeaderConfig(title = stringResource(R.string.app_name), navigationIcon = null, onNavigationClick = {}))

    Scaffold(
        modifier = modifier,
        topBar = {
            Header(config = headerConfig)
        },
        bottomBar = {
            Footer(currentRoute = nav){
                nav = it
            }
        }
    ) {
        Surface(Modifier.padding(it)) {
            when(nav){
                "home" -> HomeScreenPreview()
                "charts" -> ChartsScreenPreview()
                "reports" -> HomeScreenPreview()
                "settings" -> ProfileScreenPreview()
                "add" -> AddNewTransactionScreenPreview()
            }
        }
    }
}

@Preview
@Composable
private fun MainContainerPreview() {
    MoneyBudgetExpenseTrackerTheme {
        MainContainer()
    }
}