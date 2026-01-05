package com.expense.tracker.feature.add

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme

@Composable
fun AddAmountScreen(backgroundColor: Color = Color(0xFFF2F3F5)) {
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

@Preview
@Composable
private fun AddAmountScreenPreview() {
    MoneyBudgetExpenseTrackerTheme {
        AddAmountScreen()
    }
}