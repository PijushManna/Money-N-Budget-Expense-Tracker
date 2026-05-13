package com.expense.tracker.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

fun Double.formatAmount(
    currencySymbol: String = ""
): String {
    val symbols = DecimalFormatSymbols(Locale("en", "IN")).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("##,##,###.####", symbols).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 4
        isGroupingUsed = true
    }

    return "${if (this < 0) "-" else ""}$currencySymbol${formatter.format(abs(this))}"
}

fun String.toFormattedDouble(): Double {
    return this
        .replace(",", "") // remove grouping separators
        .replace(Regex("[^\\d.-]"), "") // remove currency symbols and other chars
        .toDoubleOrNull() ?: 0.0
}