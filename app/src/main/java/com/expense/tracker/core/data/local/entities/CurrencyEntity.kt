package com.expense.tracker.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val code: String,        // e.g., "USD", "INR"
    val symbol: String,      // e.g., "$", "₹"
    val conversionFactor: Double // relative to base currency (e.g., INR = 1.0)
)