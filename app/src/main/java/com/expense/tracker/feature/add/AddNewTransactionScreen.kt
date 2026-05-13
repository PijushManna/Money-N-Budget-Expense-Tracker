package com.expense.tracker.feature.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.expense.tracker.core.data.local.entities.AccountWithCurrency
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.core.domain.models.incomeCategories
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.navigation.Screen
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme
import com.expense.tracker.utils.formatAmount
import com.expense.tracker.utils.toLocalDate
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun AddNewTransactionScreen(
    navController: NavController, viewModel: AddNewTransactionViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    Scaffold(topBar = {
        Header(
            config = HeaderConfig(
            title = "Add Transaction",
            navigationIcon = Icons.Default.Close,
            onNavigationClick = {
                navController.popBackStack()
            },
            actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.RecurringPayment.route)
                }) {
                    Icon(Icons.Default.EventRepeat, contentDescription = "Event Repeat")
                }
            }))
    }) { paddingValues ->
        AddNewTransactionScreenContainer(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onTabSelected = viewModel::onTabSelected,
            onCategorySelected = viewModel::onCategorySelected,
            onKeyPress = viewModel::onKeyPress,
            onTitleChange = viewModel::onTitleChange,
            onNoteChange = viewModel::onNoteChange,
            onAccountSelected = viewModel::onAccountSelected,
            onSelectAccountClick = viewModel::showAccountSelectionDialog,
            onDismissAccountDialog = viewModel::hideAccountSelectionDialog,
            onDateSelected = viewModel::onDateSelected,
            onDismissDatePicker = viewModel::hideDatePicker
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddNewTransactionScreenContainer(
    modifier: Modifier = Modifier,
    uiState: AddNewTransactionUiState,
    onTabSelected: (Int) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onKeyPress: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onAccountSelected: (AccountWithCurrency) -> Unit,
    onSelectAccountClick: () -> Unit,
    onDismissAccountDialog: () -> Unit,
    onDateSelected: (Long) -> Unit,
    onDismissDatePicker: () -> Unit
) {
    val pagerState =
        rememberPagerState(initialPage = uiState.selectedTabIndex) { uiState.tabs.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onTabSelected(pagerState.currentPage)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            TransactionTabs(
                tabs = uiState.tabs, pagerState = pagerState, onTabSelected = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
        HorizontalPager(
            state = pagerState, modifier = Modifier.weight(1.0f)
        ) { page ->
            when (page) {
                0 -> CategoryGrid(
                    modifier = Modifier.fillMaxSize(),
                    categories = incomeCategories.values.toList(),
                    selectedCategory = uiState.selectedCategory,
                    onItemClick = onCategorySelected
                )

                1 -> CategoryGrid(
                    modifier = Modifier.fillMaxSize(),
                    categories = expenseCategories.values.toList(),
                    selectedCategory = uiState.selectedCategory,
                    onItemClick = onCategorySelected
                )

                2 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Transactions will be shown here")
                    }
                }
            }
        }

        AnimatedVisibility(uiState.showNumpad) {
            AddAmountScreen(
                amount = uiState.amount,
                title = uiState.title,
                note = uiState.note,
                accounts = uiState.accounts,
                selectedAccount = uiState.selectedAccount,
                showAccountSelectionDialog = uiState.isAccountSelectionDialogVisible,
                showDatePicker = uiState.isDatePickerVisible,
                selectedDateMillis = uiState.selectedDateMillis,
                onTitleChange = onTitleChange,
                onNoteChange = onNoteChange,
                onKeyPress = onKeyPress,
                onAccountSelected = onAccountSelected,
                onSelectAccountClick = onSelectAccountClick,
                onDismissAccountDialog = onDismissAccountDialog,
                onDateSelected = onDateSelected,
                onDismissDatePicker = onDismissDatePicker
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddNewTransactionScreenPreview() {
    MoneyBudgetExpenseTrackerTheme {
        AddNewTransactionScreenContainer(
            uiState = AddNewTransactionUiState(),
            onTabSelected = {},
            onCategorySelected = {},
            onKeyPress = {},
            onTitleChange = {},
            onNoteChange = {},
            onAccountSelected = {},
            onSelectAccountClick = {},
            onDismissAccountDialog = {},
            onDateSelected = {},
            onDismissDatePicker = {}
        )
    }
}


@Composable
fun CategoryGrid(
    categories: List<Category>,
    selectedCategory: Category,
    modifier: Modifier = Modifier,
    onItemClick: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier.padding(16.dp).animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category, isSelected = category.label == selectedCategory.label
            ) {
                onItemClick(category)
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color(
            0xFFF2F2F2
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            onClick, modifier = Modifier.background(backgroundColor, shape = CircleShape)
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.label,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = category.label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionTabs(
    tabs: List<String>, pagerState: PagerState, onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
    ) {
        tabs.forEachIndexed { index, title ->
            val selected = pagerState.currentPage == index
            val selectedColor by animateColorAsState(if (selected) Color.Black else Color.Transparent)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        selectedColor
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center) {
                Text(
                    text = title,
                    color = if (selected) Color.White else Color.Black
                )
            }
        }
    }
}


@Composable
private fun AddAmountScreen(
    amount: String,
    title: String,
    note: String,
    accounts: List<AccountWithCurrency>,
    selectedAccount: AccountWithCurrency?,
    showAccountSelectionDialog: Boolean,
    showDatePicker: Boolean,
    selectedDateMillis: Long,
    onNoteChange: (String) -> Unit,
    onKeyPress: (String) -> Unit,
    onAccountSelected: (AccountWithCurrency) -> Unit,
    onSelectAccountClick: () -> Unit,
    onDismissAccountDialog: () -> Unit,
    onDateSelected: (Long) -> Unit,
    onDismissDatePicker: () -> Unit,
    onTitleChange: (String) -> Unit,
    backgroundColor: Color = Color(0xFFF2F3F5)
) {
    if (showAccountSelectionDialog) {
        SelectAccountDialog(
            accounts = accounts,
            selectedAccount = selectedAccount,
            onDismiss = onDismissAccountDialog,
            onAccountSelected = onAccountSelected
        )
    }

    if (showDatePicker) {
        TransactionDatePickerDialog(
            selectedDateMillis = selectedDateMillis,
            onDismiss = onDismissDatePicker,
            onDateSelected = onDateSelected
        )
    }

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        AmountHeader(amount, selectedAccount, onSelectAccountClick)
        Spacer(Modifier.height(16.dp))
        TitleInput(title, onTitleChange)
        Spacer(Modifier.height(12.dp))
        NoteInput(note, onNoteChange)
        Spacer(Modifier.height(16.dp))
        Keypad(
            selectedDateMillis = selectedDateMillis,
            onKeyPress = onKeyPress
        )
    }
}

@Composable
fun TitleInput(
    title: String,
    onTitleChange: (String) -> Unit
) {
    TextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = { Text("Title") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun AmountHeader(
    amount: String,
    selectedAccount: AccountWithCurrency?,
    onSelectAccountClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectAccountButton(
            selectedAccount = selectedAccount,
            onClick = onSelectAccountClick
        )

        Text(
            text = amount, fontSize = 28.sp, fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SelectAccountButton(
    selectedAccount: AccountWithCurrency?,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Icon(Icons.Outlined.Badge, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = selectedAccount?.account?.name ?: "Select Account")
    }
}

@Composable
fun SelectAccountDialog(
    accounts: List<AccountWithCurrency>,
    selectedAccount: AccountWithCurrency?,
    onDismiss: () -> Unit,
    onAccountSelected: (AccountWithCurrency) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Account") },
        text = {
            Column {
                if (accounts.isEmpty()) {
                    Text(
                        text = "No accounts available",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    accounts.forEachIndexed { index, account ->
                        AccountSelectionRow(
                            account = account,
                            selected = selectedAccount?.account?.id == account.account.id,
                            onClick = { onAccountSelected(account) }
                        )
                        if (index < accounts.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun AccountSelectionRow(
    account: AccountWithCurrency,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = account.account.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${account.account.type} | ${account.currency.symbol}${account.account.balance.formatAmount()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NoteInput(
    note: String, onNoteChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp), verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = note,
            onValueChange = onNoteChange,
            placeholder = { Text("Note : Enter a note...") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Icon(Icons.Outlined.CameraAlt, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionDatePickerDialog(
    selectedDateMillis: Long,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis.toUtcStartOfDayMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let(onDateSelected)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

val keypadKeys = listOf(
    "7", "8", "9", "Today", "4", "5", "6", "+", "1", "2", "3", "-", ".", "0", "⌫", "✓"
)

@Composable
fun Keypad(
    selectedDateMillis: Long,
    onKeyPress: (String) -> Unit
) {
    val selectedDateLabel = selectedDateMillis.toKeypadDateLabel()

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(keypadKeys) { key ->
            KeypadButton(
                label = key,
                dateLabel = selectedDateLabel
            ) {
                onKeyPress(key)
            }
        }
    }
}

@Composable
fun KeypadButton(
    label: String,
    dateLabel: String,
    onClick: () -> Unit
) {
    val isConfirm = label == "✓"
    val isToday = label == "Today"

    Box(
        modifier = Modifier
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isConfirm) MaterialTheme.colorScheme.primaryContainer else Color.White,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        when {
            isToday -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        null,
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        dateLabel,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            label == "⌫" -> Icon(Icons.AutoMirrored.Outlined.Backspace, null)

            else -> Text(label, fontSize = 22.sp)
        }
    }
}

private fun Long.toKeypadDateLabel(): String {
    val selectedDate = toLocalDate()
    return if (selectedDate == LocalDate.now()) {
        "Today"
    } else {
        selectedDate.format(DateTimeFormatter.ofPattern("dd MMM"))
    }
}

private fun Long.toUtcStartOfDayMillis(): Long {
    val selectedDate = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return selectedDate
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}
