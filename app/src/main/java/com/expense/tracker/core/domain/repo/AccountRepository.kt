package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun insertAccount(account: AccountEntity)

    fun getAllAccounts(): Flow<List<AccountEntity>>
    fun getTotalBalance():Flow<Double>
    fun getLisOfBalance():Flow<List<Double>>
}
