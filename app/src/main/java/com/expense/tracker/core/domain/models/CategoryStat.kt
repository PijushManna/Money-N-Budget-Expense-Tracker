package com.expense.tracker.core.domain.models

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Keep
data class CategoryStat(
    val title: String,
    val value: Int,
    val icon: ImageVector,
    val iconBgColor: Color
)
