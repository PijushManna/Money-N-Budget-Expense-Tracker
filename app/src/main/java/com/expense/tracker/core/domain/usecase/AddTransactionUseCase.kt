package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.domain.repo.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: TransactionEntity) {
        transactionRepository.addTransaction(transaction)
    }
}
