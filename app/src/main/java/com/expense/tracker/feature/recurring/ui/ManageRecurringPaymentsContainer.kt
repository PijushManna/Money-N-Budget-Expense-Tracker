package com.expense.tracker.feature.recurring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.expense.tracker.feature.common.ConfirmationDialog
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.feature.recurring.RecurringPaymentUi
import com.expense.tracker.feature.recurring.viewmodel.ManageRecurringPaymentViewModel

@Composable
fun ManageRecurringPaymentsScreen(viewModel: ManageRecurringPaymentViewModel = hiltViewModel(), navController: NavController){
    val recurringPayments by viewModel.allPayments.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedPaymentId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            Header(HeaderConfig(title = "Manage Recurring Payments", navigationIcon = Icons.AutoMirrored.Default.ArrowBack, onNavigationClick = { navController.popBackStack() }))
        }
    ) {

        ManageRecurringPaymentsContainer(
            modifier = Modifier.padding(it),
            items = recurringPayments,
            onActiveChange = { id, active ->
                viewModel.updateRecurringPayment(id, active)
            },
            onDelete = { id ->
                viewModel.deleteRecurringPayment(id)
            }
        )

        ConfirmationDialog(
            visible = showDeleteDialog,
            title = "Delete Recurring Payment",
            message = "Are you sure you want to delete this recurring payment? " +
                    "Future transactions will not be generated.",
            confirmText = "Yes, Delete",
            dismissText = "No, Cancel",
            confirmColor = MaterialTheme.colorScheme.error,
            onConfirm = {
                selectedPaymentId?.let { id ->
                    viewModel.deleteRecurringPayment(id)
                }
                showDeleteDialog = false
                selectedPaymentId = null
            },
            onDismiss = {
                showDeleteDialog = false
                selectedPaymentId = null
            }
        )
    }
}

@Composable
fun ManageRecurringPaymentsContainer(
    modifier: Modifier = Modifier,
    items: List<RecurringPaymentUi>,
    onActiveChange: (Long, Boolean) -> Unit,
    onDelete: (Long) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items, key = { it.id }) { item ->
            ManageRecurringPaymentRow(
                title = item.title,
                amountText = item.amountText,
                frequencyLabel = item.frequencyLabel,
                isActive = item.isActive,
                onActiveChange = { active ->
                    onActiveChange(item.id, active)
                },
                onDeleteClick = {
                    onDelete(item.id)
                }
            )
        }
    }
}

@Composable
fun ManageRecurringPaymentRow(
    title: String,
    amountText: String,
    frequencyLabel: String,
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.Autorenew,
            contentDescription = "Recurring",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = if (isActive) 0.85f else 0.45f
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = frequencyLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        Text(
            text = amountText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(
                alpha = if (isActive) 0.85f else 0.45f
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = isActive,
            onCheckedChange = onActiveChange
        )

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageRecurringPaymentRowPreview() {
    MaterialTheme {
        ManageRecurringPaymentRow(
            title = "Netflix Subscription",
            amountText = "-₹199",
            frequencyLabel = "Monthly",
            isActive = true,
            onActiveChange = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ManageRecurringPaymentsContainerPreview() {
    val sampleItems = listOf(
        RecurringPaymentUi(
            id = 1,
            title = "Netflix Subscription",
            amountText = "-₹199",
            frequencyLabel = "Monthly",
            isActive = true
        ),
        RecurringPaymentUi(
            id = 2,
            title = "Spotify",
            amountText = "-₹129",
            frequencyLabel = "Monthly",
            isActive = false
        ),
        RecurringPaymentUi(
            id = 3,
            title = "Salary",
            amountText = "+₹45,000",
            frequencyLabel = "Monthly",
            isActive = true
        )
    )

    MaterialTheme {
        ManageRecurringPaymentsContainer(
            items = sampleItems,
            onActiveChange = { _, _ -> },
            onDelete = {}
        )
    }
}

