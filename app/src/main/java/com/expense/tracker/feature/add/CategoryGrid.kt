package com.expense.tracker.feature.add

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.ui.theme.MoneyBudgetExpenseTrackerTheme

@Composable
fun ColumnScope.CategoryGrid(categories: List<Category>, modifier: Modifier = Modifier) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .weight(1F)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category, isSelected = category == expenseCategories.first())
        }
    }
}

@Composable
private fun CategoryItem(category: Category, isSelected: Boolean = false) {
    val backgroundColor by animateColorAsState( if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color(0xFFF2F2F2))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
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

@Preview(showBackground = true)
@Composable
private fun CategoryGridPreview() {
    MoneyBudgetExpenseTrackerTheme {
        Column {
            CategoryGrid(
                categories = expenseCategories
            )
        }
    }
}