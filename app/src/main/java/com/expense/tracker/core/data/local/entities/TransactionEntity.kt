package com.expense.tracker.core.data.local.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Keep
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,
    val amount: Double,

    val type: TransactionType,
    val categoryId: Long,
    val accountId: Long = 0L,

    val timestamp: Long = System.currentTimeMillis(),
    val note: String? = null,
    val currency: String = "₹"
)

@Keep
@Serializable
enum class TransactionType{
    INCOME,
    EXPENSE
}
