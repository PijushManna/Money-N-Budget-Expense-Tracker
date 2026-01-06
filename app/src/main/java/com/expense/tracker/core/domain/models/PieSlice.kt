package com.expense.tracker.core.domain.models

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color

@Keep
data class PieSlice(
    val label: String,
    val value: Int,
    val color: Color
)