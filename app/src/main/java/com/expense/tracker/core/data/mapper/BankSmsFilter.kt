package com.expense.tracker.core.data.mapper

object BankSmsFilter {

    private val bankSenders = listOf(
        "HDFCBK",
        "ICICIB",
        "SBIBNK",
        "AXISBK",
        "KOTAKB",
        "IDFCFB",
        "VK-UPI",
        "UPI",
        "PAYTM",
        "GPAY"
    )

    fun isBankSms(sender: String): Boolean {
        return bankSenders.any {
            sender.uppercase().contains(it)
        }
    }
}