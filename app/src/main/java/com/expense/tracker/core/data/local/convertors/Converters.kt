package com.expense.tracker.core.data.local.convertors

import androidx.room.TypeConverter
import com.expense.tracker.core.data.local.entities.TransactionType

class Converters {

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType =
        TransactionType.valueOf(value)
}
