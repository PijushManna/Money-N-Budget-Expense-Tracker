package com.expense.tracker.feature.home.states

data class DateRangeResult(
    val start: Long,
    val end: Long,
    val label: String // 👈 for UI display
)