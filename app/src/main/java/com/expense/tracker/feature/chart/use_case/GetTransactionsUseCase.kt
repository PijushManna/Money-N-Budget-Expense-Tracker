package com.expense.tracker.feature.chart.use_case

import com.expense.tracker.core.domain.repo.TransactionRepository
import java.time.LocalDate
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate) = repository.getTransactionsBetween(startDate, endDate)
}