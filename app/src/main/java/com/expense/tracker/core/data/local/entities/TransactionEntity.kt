package com.expense.tracker.core.data.local.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Keep
@Entity(
    tableName = "transactions",
    indices = [Index(value = ["smsId"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = AccountEntity::class,
        parentColumns = ["id"],
        childColumns = ["accountId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val smsId: Long,
    val amount: Double,
    val type: TransactionType,
    val categoryName: String = "",
    val accountId: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String? = null,
)

@Keep
@Serializable
enum class TransactionType {
    INCOME, EXPENSE
}
