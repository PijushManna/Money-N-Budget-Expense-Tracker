package com.expense.tracker.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreenContainer(state: DetailsState, onBack: () -> Unit, onEdit: (id: Long) -> Unit = {}, onDelete: () -> Unit = {}) {
    Scaffold(
        topBar = {
            Header(HeaderConfig(title = "Transaction Details", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = { onBack() }))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    DetailItem(
                        label = "Title",
                        value = state.title
                    )
                    DetailItem(
                        label = "Amount",
                        value = "${state.currency} ${state.amount}"
                    )
                    DetailItem(
                        label = "Transaction Type",
                        value = state.type.name.lowercase().replaceFirstChar { it.titlecase() }
                    )
                    DetailItem(
                        label = "Category",
                        value = state.category
                    )
                    DetailItem(
                        label = "Date",
                        value = state.date
                    )
                    if (state.note.isNotBlank()) {
                        DetailItem(
                            label = "Note",
                            value = state.note
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onDelete() }) {
                    Text(text = "Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    onEdit(state.id)
                }) {
                    Text(text = "Edit")
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
