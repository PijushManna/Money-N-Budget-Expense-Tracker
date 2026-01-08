package com.expense.tracker.feature.chart

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.expense.tracker.utils.formatAmount

@Composable
fun AnimatedDonutChart(
    data: List<Pair<String, Double>>,
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
        PieSlice(item.first, item.second.toInt(), colors[index % colors.size])
    }

    val total = data.sumOf { it.second }

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
                    val sweepAngle = maxOf((slice.value.toDouble() / total) * 360.0 * animatedProgress - gapDegrees, 0.0).toFloat()

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
                text = total.formatAmount(),
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
                    Text(text = "${slice.label} (${data[index].second.formatAmount()})")
                }
            }
        }
    }
}

@Preview
@Composable
fun AnimatedDonutChartPreview() {
    val chartData = listOf(
        "Red" to 40.0,
        "Blue" to 30.0,
        "Green" to 20.0,
        "Yellow" to 10.0
    )

    AnimatedDonutChart(data = chartData, modifier = Modifier.padding(16.dp))
}