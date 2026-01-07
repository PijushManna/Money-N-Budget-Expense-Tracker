package com.expense.tracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.expense.tracker.core.data.local.entities.RecurringPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringPaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: RecurringPaymentEntity)

    @Update
    suspend fun update(payment: RecurringPaymentEntity)

    @Delete
    suspend fun delete(payment: RecurringPaymentEntity)

    @Query("SELECT * FROM recurring_payments WHERE isActive = 1")
    fun getActiveRecurringPayments(): Flow<List<RecurringPaymentEntity>>

    @Query("SELECT * FROM recurring_payments WHERE id = :id")
    suspend fun getById(id: Long): RecurringPaymentEntity?

    @Query("UPDATE recurring_payments SET isActive = 0 WHERE id = :id")
    suspend fun deactivate(id: Long)
}
