package com.expense.tracker.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

fun LocalDate.toUiDate(): String =
    format(java.time.format.DateTimeFormatter.ofPattern("d MMM, EEEE"))
