package com.expense.tracker.core.data.local.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "recurring_payments")
data class RecurringPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val amount: Double,
    val type: TransactionType, // INCOME / EXPENSE
    val cid: Long,
    val frequency: RecurringFrequency,
    val startDate: Long, // millis
    val isActive: Boolean = true
)

@Keep
@Serializable
enum class RecurringFrequency {
    DAILY, WEEKLY, BIWEEKLY, MONTHLY, QUARTERLY, HALFYEARLY, YEARLY
}
