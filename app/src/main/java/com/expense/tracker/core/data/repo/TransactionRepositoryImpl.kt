package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.TransactionDao
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.utils.toLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
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

    override fun getTransactionsBetween(startDate: LocalDate, endDate: LocalDate): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsBetween(startDate.toLong(), endDate.toLong())
    }

    override fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByType(type)
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