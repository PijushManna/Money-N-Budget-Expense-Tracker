package com.expense.tracker.feature.chart.use_case

import com.expense.tracker.core.domain.repo.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(startDate: Long, endDate: Long) = repository.getTransactionsBetween(startDate, endDate)
}