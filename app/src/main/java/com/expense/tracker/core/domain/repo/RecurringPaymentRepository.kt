package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.RecurringPaymentEntity
import kotlinx.coroutines.flow.Flow

interface RecurringPaymentRepository {
    fun getAllRecurringPayments(): Flow<List<RecurringPaymentEntity>>
    fun getActiveRecurringPayments(): Flow<List<RecurringPaymentEntity>>
    suspend fun getRecurringPaymentById(id: Long): RecurringPaymentEntity?
    suspend fun insertRecurringPayment(payment: RecurringPaymentEntity)
    suspend fun updateRecurringPayment(payment: RecurringPaymentEntity)
    suspend fun deleteRecurringPayment(paymentId: Long)
    suspend fun deactivateRecurringPayment(id: Long)
}
