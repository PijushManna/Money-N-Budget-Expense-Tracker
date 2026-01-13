package com.expense.tracker.feature.budget.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.tracker.core.data.local.entities.CategoryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBudgetBottomSheet(
    category: CategoryEntity,
    initialAmount: Double?,
    onSave: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember {
        mutableStateOf(initialAmount?.toString() ?: "")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "Set Budget for ${category.name}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    amount.toDoubleOrNull()?.let(onSave)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Budget")
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}
