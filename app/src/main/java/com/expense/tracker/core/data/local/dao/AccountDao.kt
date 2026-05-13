package com.expense.tracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.expense.tracker.core.data.local.entities.AccountEntity
import com.expense.tracker.core.data.local.entities.AccountWithCurrency
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao : BaseDao<AccountEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Transaction
    @Query("SELECT * FROM accounts")
     fun getAllAccounts():Flow<List<AccountWithCurrency>>

    @Query("SELECT SUM(balance) FROM accounts")
    fun getTotalBalance(): Flow<Double>

    @Query("SELECT balance FROM accounts")
    fun getLisOfBalance(): Flow<List<Double>>

    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun count(): Int
}
