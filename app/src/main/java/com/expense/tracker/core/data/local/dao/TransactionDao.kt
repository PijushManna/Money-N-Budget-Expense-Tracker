package com.expense.tracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE type = :type 
        AND timestamp BETWEEN :start AND :end
    """)
    fun getTransactionsByType(
        type: TransactionType,
        start: Long,
        end: Long
    ): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalAmount(type: TransactionType): Flow<Double?>
}
