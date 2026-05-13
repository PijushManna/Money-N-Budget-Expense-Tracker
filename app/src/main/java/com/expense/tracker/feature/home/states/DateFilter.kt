package com.expense.tracker.feature.home.states

sealed class DateFilter {
    data class MonthYear(val month: Int, val year: Int) : DateFilter()
    object Last3Months : DateFilter()
    object Last6Months : DateFilter()
    object Last1Year : DateFilter()
    object All : DateFilter()

    override fun toString(): String {
        return when (this) {
            is MonthYear -> "Select Month & Year"
            is Last3Months -> "Last 3 Months"
            is Last6Months -> "Last 6 Months"
            is Last1Year -> "Last 1 Year"
            is All -> "All Time"
        }
    }
}