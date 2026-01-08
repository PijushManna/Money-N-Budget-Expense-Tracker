package com.expense.tracker.feature.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.utils.toLocalDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreenContent(
    uiState: ChartsUiState,
    navController: NavController,
    onEvent: (ChartsEvent) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onEvent(ChartsEvent.ChangeDate(it.toLocalDate()))
                    }
                    showDatePicker = false
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
            FilterTabs(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = { onEvent(ChartsEvent.FilterBy(it)) }
            )
            TextButton(onClick = { showDatePicker = true }) {
                Text(text = "Select Date")
            }

            if (uiState.categoryStats.isNotEmpty()) {
                AnimatedDonutChart(
                    data = uiState.categoryStats.map { it.title to it.value }
                )
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp)
            if (uiState.categoryStats.isNotEmpty()) {
                CategoryStats(
                    stats = uiState.categoryStats
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
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        filters.forEach { filter ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedFilter == filter) Color.DarkGray else Color.Transparent)
                    .clickable { onFilterSelected(filter) }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filter,
                    color = if (selectedFilter == filter) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChartsScreenPreview() {
    val uiState = ChartsUiState(
        selectedFilter = "Monthly",
        selectedDate = LocalDate.now()
    )

    ChartsScreenContent(
        uiState = uiState,
        navController = rememberNavController(),
        onEvent = {}
    )
}
