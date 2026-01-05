package com.expense.tracker.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.tooling.preview.Preview
import com.expense.tracker.R
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.feature.common.rememberHeaderConfig
import com.expense.tracker.feature.home.HomeScreen
import com.expense.tracker.feature.home.HomeScreenPreview
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme

@Composable
fun MainContainer(modifier: Modifier = Modifier) {
    val headerConfig by rememberHeaderConfig(HeaderConfig(title = stringResource(R.string.app_name), navigationIcon = null))

    Scaffold(
        modifier = modifier,
        topBar = {
            Header(config = headerConfig)
        },
        bottomBar = {
            Footer(currentRoute = "home")
        }
    ) {
        Surface(Modifier.padding(it)) {
            HomeScreenPreview()
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