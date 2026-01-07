package com.expense.tracker.feature.recurring.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.tracker.core.data.local.entities.RecurringFrequency
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.feature.recurring.viewmodel.RecurringPaymentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecurringPaymentScreen(
    viewModel: RecurringPaymentViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    RecurringPaymentScreen(
        uiState = uiState,
        onTitleChange = viewModel::onTitleChange,
        onAmountChange = viewModel::onAmountChange,
        onTypeChange = viewModel::onTypeChange,
        onFrequencyChange = viewModel::onFrequencyChange,
        onStartDateChange = viewModel::onStartDateChange,
        onActiveChange = viewModel::onActiveChange,
        onDismiss = onNavigateUp,
        onSave = {
            viewModel.addRecurringPayment()
            onNavigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecurringPaymentScreen(
    uiState: RecurringPaymentState = RecurringPaymentState(),
    onTitleChange: (String) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
    onTypeChange: (TransactionType) -> Unit = {},
    onFrequencyChange: (RecurringFrequency) -> Unit = {},
    onStartDateChange: (Long) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    onDismiss: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Header(
                HeaderConfig(
                    title = "Add Recurring Payment",
                    navigationIcon = Icons.Default.Close,
                    onNavigationClick = {
                        onDismiss()
                    }
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            /* ---------- Title ---------- */
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.title,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                placeholder = { Text("eg. Netflix Subscription") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            /* ---------- Amount ---------- */
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.amount,
                onValueChange = onAmountChange,
                label = { Text("Amount") },
                placeholder = { Text("eg. ₹199") },
                prefix = { Text("₹") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            /* ---------- Type Selector ---------- */
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TransactionType.entries.forEach { type ->
                    FilterChip(
                        selected = uiState.type == type,
                        onClick = { onTypeChange(type) },
                        label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }


            /* ---------- Frequency ---------- */
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = uiState.frequency.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    label = { Text("Frequency") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    RecurringFrequency.entries.forEach { frequency ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    frequency.name.lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                )
                            },
                            onClick = {
                                onFrequencyChange(frequency)
                                expanded = false
                            }
                        )
                    }
                }
            }

            /* ---------- Start Date ---------- */
            Column {
                Text(
                    text = "Start Date",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showDatePicker = true }
                ) {
                    Text(
                        text = SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                        ).format(Date(uiState.startDate))
                    )
                }
            }

            /* ---------- Active Switch ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Recurring Active",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Disable to pause this payment",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = uiState.isActive,
                    onCheckedChange = onActiveChange
                )
            }
            /* ---------- Save CTA ---------- */
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.title.isNotBlank() && uiState.amount.isNotBlank()
            ) {
                Text("Save Recurring Payment")
            }
        }
    }

    /* ---------- Date Picker ---------- */
    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = uiState.startDate)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onStartDateChange)
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Preview
@Composable
private fun RecurringPaymentScreenPreview() {
    RecurringPaymentScreen()
}