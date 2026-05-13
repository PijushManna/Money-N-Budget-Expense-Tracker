package com.expense.tracker.feature.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.expense.tracker.feature.home.TransactionsViewType

@Composable
fun TransactionsListItem(
    modifier: Modifier = Modifier, item: TransactionsViewType.Transaction, onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp, horizontal = 12.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape
                    )
                    .padding(8.dp)
            )
            Column {
                Text(item.label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                CategoryPill(item.categoryName)
            }
        }
        Text(item.amount, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun CategoryPill(
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 2.dp)
            .background(
                color = Color(0xFFE8F0FE), // light background
                shape = RoundedCornerShape(10)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = name,
            color = Color(0xFF1A73E8),
            style = MaterialTheme.typography.labelMedium
        )
    }
}