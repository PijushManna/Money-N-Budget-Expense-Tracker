package com.expense.tracker.feature.home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expense.tracker.feature.home.TransactionsViewType

@Composable
fun TransactionListHeaderItem(modifier: Modifier = Modifier, item: TransactionsViewType.Header) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Text(item.date, style = MaterialTheme.typography.bodySmall)
        Text(item.total, style = MaterialTheme.typography.bodySmall)
    }
    HorizontalDivider(thickness = 0.5.dp)
}