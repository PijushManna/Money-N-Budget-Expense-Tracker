package com.expense.tracker.feature.chart

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.expense.tracker.core.domain.models.PieSlice

@Composable
fun AnimatedDonutChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    // Convert input data into PieSlices with colors
    val colors = listOf(
        Color(0xFFEF5350),
        Color(0xFFAB47BC),
        Color(0xFF42A5F5),
        Color(0xFF26A69A),
        Color(0xFFFFCA28),
        Color(0xFFFF7043)
    )

    val slices = data.mapIndexed { index, item ->
        PieSlice(item.first, item.second, colors[index % colors.size])
    }

    val total = slices.sumOf { it.value }

    var selectedIndex by remember { mutableStateOf(-1) }

    // Animation progress from 0f to 1f
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier
                .size(150.dp)
                .padding(8.dp)
                .clickable { /* optional: click on empty space */ }
            ) {
                var startAngle = -90f
                val gapDegrees = 2f
                slices.forEachIndexed { index, slice ->
                    val sweepAngle = maxOf((slice.value.toFloat() / total) * 360f * animatedProgress - gapDegrees, 0F)

                    val isSelected = index == selectedIndex
                    drawArc(
                        color = slice.color,
                        startAngle = startAngle + gapDegrees / 2,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = if (isSelected) 100f else 80f, cap = StrokeCap.Butt )
                    )

                    startAngle += sweepAngle + gapDegrees
                }
            }

            // Center text showing total
            Text(
                text = "$total",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                lineHeight = 25.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Legend
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            slices.forEachIndexed { index, slice ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable { selectedIndex = index },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(slice.color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${slice.label} (${slice.value})")
                }
            }
        }
    }
}

@Preview
@Composable
fun AnimatedDonutChartPreview() {
    val chartData = listOf(
        "Red" to 40,
        "Blue" to 30,
        "Green" to 20,
        "Yellow" to 10
    )

    AnimatedDonutChart(data = chartData, modifier = Modifier.padding(16.dp))
}