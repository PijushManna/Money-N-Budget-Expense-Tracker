package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun addTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)
    suspend fun deleteById(id: Long)
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    fun getFirstTransaction(): Flow<TransactionEntity?>
    fun getTotalIncome(): Flow<Double>
    fun getTotalExpense(): Flow<Double>
    fun getTransactionById(id: Long): Flow<TransactionEntity?>
}
