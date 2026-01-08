package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.AccountDao
import com.expense.tracker.core.data.local.entities.AccountEntity
import com.expense.tracker.core.domain.repo.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override suspend fun insertAccount(account: AccountEntity) {
        accountDao.insertAccount(account)
    }

    override fun getAllAccounts(): Flow<List<AccountEntity>> = accountDao.getAllAccounts()
    override fun getTotalBalance(): Flow<Double> {
        return accountDao.getTotalBalance()
    }
}
