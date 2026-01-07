package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.TransactionDao
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun addTransaction(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }

    override suspend fun deleteById(id: Long) {
        transactionDao.deleteById(id)
    }

    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    override fun getFirstTransaction(): Flow<TransactionEntity?> {
        return transactionDao.getFirstTransaction()
    }

    override fun getTotalIncome(): Flow<Double> {
        return transactionDao.getTotalAmount(TransactionType.INCOME)
            .map { it ?: 0.0 }
    }

    override fun getTotalExpense(): Flow<Double> {
        return transactionDao.getTotalAmount(TransactionType.EXPENSE)
            .map { it ?: 0.0 }
    }

    override fun getTransactionById(id: Long): Flow<TransactionEntity?> {
        return transactionDao.getTransactionById(id)
    }
}
