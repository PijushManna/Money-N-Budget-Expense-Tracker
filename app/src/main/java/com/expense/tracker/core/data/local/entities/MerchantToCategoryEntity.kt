package com.expense.tracker.core.data.local.entities

import androidx.room.Entity

@Entity(tableName = "merchant_categories")
data class MerchantToCategoryEntity(
    val id: Long = 0L,
    val merchant: String,
    val categoryId: Long,
    val categoryName: String
)