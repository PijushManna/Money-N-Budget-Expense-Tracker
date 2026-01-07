package com.expense.tracker.core.data.local.convertors

import androidx.room.TypeConverter
import com.expense.tracker.core.data.local.entities.RecurringFrequency

class RecurringConverters {
    @TypeConverter
    fun fromFrequency(value: RecurringFrequency): String = value.name

    @TypeConverter
    fun toFrequency(value: String): RecurringFrequency =
        RecurringFrequency.valueOf(value)
}
