package com.expense.tracker.feature.goals

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.expense.tracker.core.data.local.entities.GoalEntity
import com.expense.tracker.core.data.local.entities.GoalPeriod
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.feature.common.Footer
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.navigation.Screen
import com.expense.tracker.utils.formatAmount

@Composable
fun GoalsScreen(
    navController: NavController,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    GoalsScreenContent(
        state = state,
        navController = navController,
        onAddGoalClick = viewModel::showAddGoalDialog,
        onDismissAddGoal = viewModel::hideAddGoalDialog,
        onSaveGoal = viewModel::addGoal,
        onDeleteGoal = viewModel::deleteGoal
    )
}

@Composable
fun GoalsScreenContent(
    state: GoalsUiState,
    navController: NavController,
    onAddGoalClick: () -> Unit,
    onDismissAddGoal: () -> Unit,
    onSaveGoal: (String, TransactionType, Double, GoalPeriod) -> Unit,
    onDeleteGoal: (GoalEntity) -> Unit
) {
    if (state.showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = onDismissAddGoal,
            onSave = onSaveGoal
        )
    }

    Scaffold(
        topBar = {
            Header(
                config = HeaderConfig(
                    title = "Goals",
                    navigationIcon = null,
                    onNavigationClick = {}
                )
            )
        },
        bottomBar = {
            Footer(currentRoute = Screen.Reports.route) {
                navController.navigate(it)
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddGoalClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Goal") }
            )
        }
    ) { paddingValues ->
        if (state.goals.isEmpty()) {
            EmptyGoalsState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                onAddGoalClick = onAddGoalClick
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.goals, key = { it.goal.id }) { goal ->
                    GoalProgressCard(
                        goal = goal,
                        onDeleteClick = { onDeleteGoal(goal.goal) }
                    )
                }
                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }
}

@Composable
private fun GoalProgressCard(
    goal: GoalProgressUi,
    onDeleteClick: () -> Unit
) {
    val statusColor = when (goal.status) {
        GoalStatus.ON_TRACK -> MaterialTheme.colorScheme.primary
        GoalStatus.EXCEEDED -> MaterialTheme.colorScheme.error
        GoalStatus.ACHIEVED -> Color(0xFF2E7D32)
    }
    val statusText = when (goal.status) {
        GoalStatus.ON_TRACK -> if (goal.goal.type == TransactionType.EXPENSE) "Within budget" else "In progress"
        GoalStatus.EXCEEDED -> "Budget exceeded"
        GoalStatus.ACHIEVED -> "Milestone achieved"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.goal.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${goal.goal.type.name.lowercase().replaceFirstChar { it.uppercase() }} • ${goal.goal.period.name.lowercase().replaceFirstChar { it.uppercase() }} • ${goal.periodLabel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete goal")
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = goal.currentAmount.formatAmount(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Target ${goal.goal.targetAmount.formatAmount()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { goal.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = statusColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = if (goal.remainingAmount >= 0.0) {
                        "${goal.remainingAmount.formatAmount()} remaining"
                    } else {
                        "${(-goal.remainingAmount).formatAmount()} over"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyGoalsState(
    modifier: Modifier,
    onAddGoalClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Flag,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No goals yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Add weekly, monthly, or yearly targets for expenses and income.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddGoalClick) {
            Text("Add Goal")
        }
    }
}

@Composable
private fun AddGoalDialog(
    onDismiss: () -> Unit,
    onSave: (String, TransactionType, Double, GoalPeriod) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var period by remember { mutableStateOf(GoalPeriod.MONTHLY) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Target Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                GoalChoiceRow(
                    title = "Type",
                    options = listOf(TransactionType.EXPENSE, TransactionType.INCOME),
                    selected = type,
                    label = { it.name.lowercase().replaceFirstChar { char -> char.uppercase() } },
                    onSelected = { type = it }
                )
                GoalChoiceRow(
                    title = "Period",
                    options = GoalPeriod.entries,
                    selected = period,
                    label = { it.name.lowercase().replaceFirstChar { char -> char.uppercase() } },
                    onSelected = { period = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0.0,
                onClick = {
                    onSave(title, type, amount.toDoubleOrNull() ?: 0.0, period)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun <T> GoalChoiceRow(
    title: String,
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelected: (T) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selected,
                    onClick = { onSelected(option) },
                    label = { Text(label(option)) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalsScreenPreview() {
    GoalsScreenContent(
        state = GoalsUiState(
            goals = listOf(
                GoalProgressUi(
                    goal = GoalEntity(
                        id = 1,
                        title = "Monthly expenses",
                        type = TransactionType.EXPENSE,
                        targetAmount = 20000.0,
                        period = GoalPeriod.MONTHLY
                    ),
                    currentAmount = 13500.0,
                    progress = 0.675f,
                    remainingAmount = 6500.0,
                    periodLabel = "May 2026",
                    status = GoalStatus.ON_TRACK
                )
            )
        ),
        navController = rememberNavController(),
        onAddGoalClick = {},
        onDismissAddGoal = {},
        onSaveGoal = { _, _, _, _ -> },
        onDeleteGoal = {}
    )
}
