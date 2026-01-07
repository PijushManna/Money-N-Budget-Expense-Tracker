package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.RecurringPaymentEntity
import kotlinx.coroutines.flow.Flow

interface RecurringPaymentRepository {
    fun getActiveRecurringPayments(): Flow<List<RecurringPaymentEntity>>
    suspend fun getRecurringPaymentById(id: Long): RecurringPaymentEntity?
    suspend fun insertRecurringPayment(payment: RecurringPaymentEntity)
    suspend fun updateRecurringPayment(payment: RecurringPaymentEntity)
    suspend fun deleteRecurringPayment(payment: RecurringPaymentEntity)
    suspend fun deactivateRecurringPayment(id: Long)
}
