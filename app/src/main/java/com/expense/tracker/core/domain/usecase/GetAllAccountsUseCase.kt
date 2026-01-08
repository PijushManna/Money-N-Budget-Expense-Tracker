package com.expense.tracker.core.domain.usecase

import com.expense.tracker.core.domain.repo.AccountRepository
import javax.inject.Inject

class GetAllAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke() = accountRepository.getAllAccounts()
}
