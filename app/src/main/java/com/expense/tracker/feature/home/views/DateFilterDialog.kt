package com.expense.tracker.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expense.tracker.feature.home.states.DateFilter
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DateFilterDialog(
    selectedFilter: DateFilter,
    onDismiss: () -> Unit,
    onApply: (DateFilter) -> Unit
) {

    var tempFilter by remember { mutableStateOf(selectedFilter) }

    val now = LocalDate.now()
    val months = Month.entries.map {
        it.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }
    val years = (2020..now.year).toList().reversed()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onApply(tempFilter) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Select Time Range") },
        text = {

            Column {

                // 🔘 Month-Year Option
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tempFilter is DateFilter.MonthYear,
                        onClick = {
                            tempFilter = DateFilter.MonthYear(
                                month = now.monthValue,
                                year = now.year
                            )
                        }
                    )
                    Text("Select Month & Year")
                }

                if (tempFilter is DateFilter.MonthYear) {

                    val selected = tempFilter as DateFilter.MonthYear

                    Spacer(Modifier.height(8.dp))

                    Row {

                        // Month dropdown
                        DropdownSelector(
                            label = "Month",
                            options = months,
                            selected = months[selected.month - 1],
                            onSelected = {
                                val monthIndex = months.indexOf(it) + 1
                                tempFilter = selected.copy(month = monthIndex)
                            }
                        )

                        Spacer(Modifier.width(8.dp))

                        // Year dropdown
                        DropdownSelector(
                            label = "Year",
                            options = years.map { it.toString() },
                            selected = selected.year.toString(),
                            onSelected = {
                                tempFilter = selected.copy(year = it.toInt())
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 🔘 Quick Options
                FilterOption("Last 3 Months", tempFilter is DateFilter.Last3Months) {
                    tempFilter = DateFilter.Last3Months
                }

                FilterOption("Last 6 Months", tempFilter is DateFilter.Last6Months) {
                    tempFilter = DateFilter.Last6Months
                }

                FilterOption("Last 1 Year", tempFilter is DateFilter.Last1Year) {
                    tempFilter = DateFilter.Last1Year
                }

                FilterOption("All Transactions", tempFilter is DateFilter.All) {
                    tempFilter = DateFilter.All
                }
            }
        }
    )
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .width(120.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FilterOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(label)
    }
}

@Preview
@Composable
private fun DateFilterDialogPreview() {
    DateFilterDialog(selectedFilter = DateFilter.Last3Months, onDismiss = {}, onApply = {})
}