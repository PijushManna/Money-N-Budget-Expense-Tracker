package com.expense.tracker.feature.home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.expense.tracker.R
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
        Text(item.date, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically){
            if (item.expense.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.expense),
                    contentDescription = "Expense Icon",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.expense, style = MaterialTheme.typography.bodyMedium)
            }
            if (item.expense.isNotEmpty() && item.income.isNotEmpty()) {
                Text(" | ", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 4.dp))
            }
            if (item.income.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.income),
                    contentDescription = "Income Icon",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.income, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
    HorizontalDivider(thickness = 0.5.dp)
}