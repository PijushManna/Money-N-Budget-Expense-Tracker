package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.data.mapper.TransactionParser
import com.expense.tracker.core.domain.repo.SmsRepo
import com.expense.tracker.core.domain.repo.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddSmsTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val smsRepo: SmsRepo
) {
    suspend operator fun invoke() {
        withContext(Dispatchers.Default) {
            val smsList = smsRepo.getAllSms()
            smsList.mapNotNull { TransactionParser.parse(it) }.forEach {
                transactionRepository.addTransaction(it)
            }
        }
    }
}