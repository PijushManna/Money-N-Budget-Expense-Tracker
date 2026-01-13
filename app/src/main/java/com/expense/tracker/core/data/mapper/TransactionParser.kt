package com.expense.tracker.core.data.mapper

import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.SmsMessage

object TransactionParser {
    // Amount patterns used by Indian banks
    private val amountRegex =
        Regex("(?:rs\\.?|inr|₹)\\s?([0-9,]+\\.?[0-9]*)",
            RegexOption.IGNORE_CASE)

    private val expenseKeywords = listOf(
        "debited", "spent", "paid", "purchase",
        "txn", "withdrawn", "sent"
    )

    private val incomeKeywords = listOf(
        "credited", "received", "deposited"
    )

    private val categoryRules by lazy {
        mapOf(

            "Food" to listOf(
                "SWIGGY", "ZOMATO", "UBEREATS", "DOMINOS", "PIZZA", "MCDONALD"
            ),

            "Travel" to listOf(
                "UBER", "OLA", "RAPIDO", "IRCTC", "MAKEMYTRIP", "GOIBIBO"
            ),

            "Shopping" to listOf(
                "AMAZON", "FLIPKART", "MYNTRA", "AJIO", "MEESHO"
            ),

            "Entertainment" to listOf(
                "NETFLIX", "SPOTIFY", "HOTSTAR", "SONYLIV", "BOOKMYSHOW"
            ),

            "Bills" to listOf(
                "BESCOM", "AIRTEL", "JIO", "VODAFONE", "ACTFIBER"
            ),

            "Health" to listOf(
                "PHARMA", "APOLLO", "MEDPLUS"
            ),

            "Transportation" to listOf(
                "METRO", "BMTC"
            )
        )
    }

    fun extractCategory(sms: String): String {
        val text = sms.uppercase()

        for ((category, keywords) in categoryRules) {
            if (keywords.any { text.contains(it) }) {
                return category
            }
        }
        return "Others"
    }

    fun parse(sms: SmsMessage): TransactionEntity? {

        val body = sms.body.lowercase()

        // Step 1: Extract Amount
        val amountMatch = amountRegex.find(body)
            ?: return null

        val amount = amountMatch.groupValues[1]
            .replace(",", "")
            .toDoubleOrNull()
            ?: return null

        // Step 2: Detect Type
        val type = when {
            expenseKeywords.any { body.contains(it) } ->
                TransactionType.EXPENSE

            incomeKeywords.any { body.contains(it) } ->
                TransactionType.INCOME

            else -> return null
        }

        // Step 3: Extract Merchant
        val merchant = extractMerchant(body)

        return TransactionEntity(
            title = merchant ?: "",
            amount = amount,
            type = type,
            categoryName = extractCategory(sms.body),
            note = sms.body,
            currency = "₹",
            accountId = 0,
            smsId = sms.id,
            timestamp = sms.date
        )
    }

    private fun extractMerchant(body: String): String? {

        val merchantRegex =
            Regex("(?:at|to|from)\\s([a-zA-Z0-9\\-\\s]{2,30})")

        return merchantRegex.find(body)
            ?.groupValues?.get(1)
            ?.trim()
    }
}