package com.expense.tracker.core.data.local.entities

import android.icu.util.Calendar
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "recurring_payments")
data class RecurringPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val cid: Long = 106,
    val frequency: RecurringFrequency,
    val startDate: Long, // millis
    val nextHandlingDate: Long = startDate,
    val isActive: Boolean = true
){
    fun calculateNextHandlingDate(): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = nextHandlingDate
        }

        when (frequency) {
            RecurringFrequency.DAILY ->
                calendar.add(Calendar.DAY_OF_YEAR, 1)

            RecurringFrequency.WEEKLY ->
                calendar.add(Calendar.WEEK_OF_YEAR, 1)

            RecurringFrequency.BIWEEKLY ->
                calendar.add(Calendar.WEEK_OF_YEAR, 2)

            RecurringFrequency.MONTHLY ->
                calendar.add(Calendar.MONTH, 1)

            RecurringFrequency.QUARTERLY ->
                calendar.add(Calendar.MONTH, 3)

            RecurringFrequency.HALFYEARLY ->
                calendar.add(Calendar.MONTH, 6)

            RecurringFrequency.YEARLY ->
                calendar.add(Calendar.YEAR, 1)
        }

        return calendar.timeInMillis
    }
}

@Keep
@Serializable
enum class RecurringFrequency {
    DAILY, WEEKLY, BIWEEKLY, MONTHLY, QUARTERLY, HALFYEARLY, YEARLY
}
