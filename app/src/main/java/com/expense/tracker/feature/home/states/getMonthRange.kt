package com.expense.tracker.feature.home.states

import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

fun getMonthRange(year: Int, month: Int): DateRangeResult {

    val start = LocalDate.of(year, month-1, 1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()


    val end = LocalDate.of(year, month, 1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    // Month name
    val monthName = Month.of(month).name.lowercase()
        .replaceFirstChar { it.uppercase() }

    return DateRangeResult(
        start = start,
        end = end,
        label = "$monthName $year"
    )
}


fun getDateRange(filter: DateFilter): DateRangeResult {
    val zone = ZoneId.systemDefault()
    val now = ZonedDateTime.now(zone)

    return when (filter) {

        is DateFilter.MonthYear -> {
            val startDate = LocalDate.of(filter.year, filter.month, 1)
            val endDate = startDate.plusMonths(1).minusDays(1)

            val start = startDate
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()

            val end = endDate
                .atTime(LocalTime.MAX)
                .atZone(zone)
                .toInstant()
                .toEpochMilli()

            val monthName = Month.of(filter.month)
                .getDisplayName(TextStyle.FULL, Locale.getDefault())

            DateRangeResult(
                start = start,
                end = end,
                label = "$monthName ${filter.year}"
            )
        }

        DateFilter.Last3Months -> {
            val start = now.minusMonths(3)
                .toInstant()
                .toEpochMilli()

            val end = now.toInstant().toEpochMilli()

            DateRangeResult(start, end, "Last 3 Months")
        }

        DateFilter.Last6Months -> {
            val start = now.minusMonths(6)
                .toInstant()
                .toEpochMilli()

            val end = now.toInstant().toEpochMilli()

            DateRangeResult(start, end, "Last 6 Months")
        }

        DateFilter.Last1Year -> {
            val start = now.minusYears(1)
                .toInstant()
                .toEpochMilli()

            val end = now.toInstant().toEpochMilli()

            DateRangeResult(start, end, "Last 1 Year")
        }

        DateFilter.All -> {
            DateRangeResult(
                start = 0L,
                end = now.toInstant().toEpochMilli(),
                label = "All Time"
            )
        }
    }
}