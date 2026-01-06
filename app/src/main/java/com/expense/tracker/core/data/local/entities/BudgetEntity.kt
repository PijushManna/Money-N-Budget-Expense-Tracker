package com.expense.tracker.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val categoryId: Long,
    val limitAmount: Double,

    // yyyy-MM format → "2026-01"
    val month: String
)
