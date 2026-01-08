package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.data.local.entities.AccountEntity
import com.expense.tracker.core.domain.repo.AccountRepository
import javax.inject.Inject

class AddAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: AccountEntity) = accountRepository.insertAccount(account)
}
