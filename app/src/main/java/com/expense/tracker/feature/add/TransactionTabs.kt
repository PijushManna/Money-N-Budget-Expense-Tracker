package com.expense.tracker.feature.add

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

@Preview
@Composable
private fun TransactionTabsPreview() {
    TransactionTabs(
        tabs = listOf("All", "Income", "Expense"),
        pagerState = PagerState(0){3}
    )
}
