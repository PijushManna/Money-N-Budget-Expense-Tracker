package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.RecurringPaymentDao
import com.expense.tracker.core.data.local.entities.RecurringPaymentEntity
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecurringPaymentRepositoryImpl @Inject constructor(
    private val recurringPaymentDao: RecurringPaymentDao
) : RecurringPaymentRepository {
    override fun getActiveRecurringPayments(): Flow<List<RecurringPaymentEntity>> {
        return recurringPaymentDao.getActiveRecurringPayments()
    }

    override suspend fun getRecurringPaymentById(id: Long): RecurringPaymentEntity? {
        return recurringPaymentDao.getById(id)
    }

    override suspend fun insertRecurringPayment(payment: RecurringPaymentEntity) {
        recurringPaymentDao.insert(payment)
    }

    override suspend fun updateRecurringPayment(payment: RecurringPaymentEntity) {
        recurringPaymentDao.update(payment)
    }

    override suspend fun deleteRecurringPayment(payment: RecurringPaymentEntity) {
        recurringPaymentDao.delete(payment)
    }

    override suspend fun deactivateRecurringPayment(id: Long) {
        recurringPaymentDao.deactivate(id)
    }
}
