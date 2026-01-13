package com.expense.tracker.utils

import android.icu.util.Calendar

fun getMonthRange(year: Int, month: Int): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    calendar.set(year, month, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val start = calendar.timeInMillis

    calendar.add(Calendar.MONTH, 1)
    calendar.add(Calendar.MILLISECOND, -1)
    val end = calendar.timeInMillis

    return start to end
}