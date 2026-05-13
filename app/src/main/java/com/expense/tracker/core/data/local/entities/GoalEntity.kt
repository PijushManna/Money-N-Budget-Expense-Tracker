package com.expense.tracker.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val type: TransactionType,
    val targetAmount: Double,
    val period: GoalPeriod,
    val createdAt: Long = System.currentTimeMillis()
)

enum class GoalPeriod {
    WEEKLY, MONTHLY, YEARLY
}
