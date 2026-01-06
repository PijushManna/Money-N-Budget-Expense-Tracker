package com.expense.tracker.feature.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.expense.tracker.core.domain.models.CategoryStat

@Composable
fun CategoryStats(
    stats: List<CategoryStat>,
    modifier: Modifier = Modifier
) {
    val total = stats.sumOf { it.value }.toFloat()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(stats) { stat ->
            val percentage = if (total == 0f) 0f else stat.value / total

            CategoryStatRow(
                title = stat.title,
                value = stat.value,
                percentage = percentage,
                icon = stat.icon,
                iconBgColor = stat.iconBgColor
            )
        }
    }
}

@Composable
fun CategoryStatRow(
    title: String,
    value: Int,
    percentage: Float,
    icon: ImageVector,
    iconBgColor: Color
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = String.format("%.2f%%", percentage * 100),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = {
                        percentage
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50)),
                    color = Color(0xFFFFD54F),
                    trackColor = Color(0xFFFFF3CD)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
fun PreviewCategoryStats() {
    val stats = listOf(
        CategoryStat(
            title = "Sports",
            value = 500,
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            iconBgColor = Color(0xFFDCC2F2)
        ),
        CategoryStat(
            title = "Shopping",
            value = 104,
            icon = Icons.Default.ShoppingCart,
            iconBgColor = Color(0xFFF2D28C)
        )
    )

    CategoryStats(stats = stats)
}
