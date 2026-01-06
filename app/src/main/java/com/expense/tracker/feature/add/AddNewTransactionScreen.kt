package com.expense.tracker.feature.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.feature.common.Header
import com.expense.tracker.feature.common.HeaderConfig
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddNewTransactionScreen(navController: NavController) {
    Scaffold(topBar = {
        Header(
            config = HeaderConfig(
                title = "Add Transaction",
                navigationIcon = Icons.Default.Close,
                onNavigationClick = {
                    navController.popBackStack()
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.EventRepeat, contentDescription = "Event Repeat")
                    }
                }
            )
        )
    }) {
        AddNewTransactionScreenContainer(Modifier.padding(it))
    }
}

@Composable
private fun AddNewTransactionScreenContainer(modifier: Modifier = Modifier) {
    val tabs = listOf("Income", "Expenses", "Transaction")
    val pagerState = remember { PagerState(0) { tabs.size } }
    var showNumpad by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            TransactionTabs(
                tabs = tabs,
                pagerState = pagerState
            )
        }
        CategoryGrid(expenseCategories){
            if (it.label.isNotBlank()) {
                showNumpad = true
            }
        }
        AnimatedVisibility(showNumpad) {
            AddAmountScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddNewTransactionScreenPreview() {
    MoneyBudgetExpenseTrackerTheme {
        AddNewTransactionScreenContainer()
    }
}


@Composable
fun ColumnScope.CategoryGrid(categories: List<Category>, modifier: Modifier = Modifier, onItemClick:(Category) -> Unit = {}) {
    var selectedCategory by remember { mutableStateOf(Category()) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .weight(1F)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category, isSelected = category.label == selectedCategory.label){
                selectedCategory = category
                onItemClick(category)
            }
        }
    }
}

@Composable
private fun CategoryItem(category: Category, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    val backgroundColor by animateColorAsState( if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color(0xFFF2F2F2))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            onClick,
            modifier = Modifier.background(backgroundColor, shape = CircleShape)
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.label,
                tint = Color.Gray
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = category.label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionTabs(
    tabs: List<String>,
    pagerState: PagerState
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
    ) {
        tabs.forEachIndexed { index, title ->
            val selected = pagerState.currentPage == index

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (selected) Color.Black else Color.Transparent
                    )
                    .clickable {
                        CoroutineScope(Dispatchers.Main).launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Black
                )
            }
        }
    }
}


@Composable
private fun AddAmountScreen(backgroundColor: Color = Color(0xFFF2F3F5)) {
    var amount by remember { mutableStateOf("0") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .padding(16.dp)
    ) {

        AmountHeader(amount)

        Spacer(Modifier.height(16.dp))

        NoteInput(note) { note = it }

        Spacer(Modifier.height(16.dp))

        Keypad(
            onKeyPress = { key ->
                amount = handleInput(amount, key)
            }
        )
    }
}

@Composable
fun AmountHeader(amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Badge, contentDescription = null)
        }

        Text(
            text = amount,
            fontSize = 36.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NoteInput(
    note: String,
    onNoteChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
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

sealed class KeypadKey {
    data class Text(val value: String) : KeypadKey()
    data object Today : KeypadKey()
    data object Add : KeypadKey()
    data object Subtract : KeypadKey()
    data object Delete : KeypadKey()
    data object Confirm : KeypadKey()
}
val keypadKeys = listOf(
    "7", "8", "9", "Today",
    "4", "5", "6", "+",
    "1", "2", "3", "-",
    ".", "0", "⌫", "✓"
)

@Composable
fun Keypad(onKeyPress: (String) -> Unit) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(keypadKeys) { key ->
            KeypadButton(key) {
                onKeyPress(key)
            }
        }
    }
}
@Composable
fun KeypadButton(
    label: String,
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
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {
            isToday -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CalendarMonth, null, tint = MaterialTheme.colorScheme.primaryContainer)
                    Spacer(Modifier.width(6.dp))
                    Text("Today", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                }
            }

            label == "⌫" -> Icon(Icons.AutoMirrored.Outlined.Backspace, null)

            else -> Text(label, fontSize = 22.sp)
        }
    }
}

fun handleInput(current: String, key: String): String {
    return when (key) {
        "⌫" -> if (current.length > 1) current.dropLast(1) else "0"
        "✓" -> current
        "+", "-", "Today" -> current
        else -> if (current == "0") key else current + key
    }
}