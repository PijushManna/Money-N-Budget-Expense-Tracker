package com.expense.tracker.core.data.test

import android.util.Log
import com.expense.tracker.core.data.local.dao.AccountDao
import com.expense.tracker.core.data.local.dao.CurrencyDao
import com.expense.tracker.core.data.local.dao.TransactionDao
import com.expense.tracker.core.data.local.entities.AccountEntity
import com.expense.tracker.core.data.local.entities.CurrencyEntity
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType

class DummyDataSeeder(
    private val currencyDao: CurrencyDao,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) {

    suspend fun seed() {

        // ✅ 1. Insert Currencies
        val currencies = listOf(
            CurrencyEntity("INR", "₹", 1.0),
            CurrencyEntity("USD", "$", 83.0),
            CurrencyEntity("EUR", "€", 90.0)
        )
        currencyDao.insertAll(currencies)

        // ✅ 2. Insert Accounts
        val accounts = listOf(
            AccountEntity(
                name = "HDFC Bank",
                balance = 50000.0,
                type = "BANK",
                currencyCode = "INR"
            ),
            AccountEntity(
                name = "Cash Wallet",
                balance = 5000.0,
                type = "CASH",
                currencyCode = "INR"
            ),
            AccountEntity(
                name = "USD Account",
                balance = 1000.0,
                type = "BANK",
                currencyCode = "USD"
            )
        )

        val accountIds = accountDao.insertAll(accounts)
        Log.d("DummyDataSeeder", "Inserted accountIds: $accountIds")

        // ✅ 3. Insert Transactions
        val transactions = listOf(
            TransactionEntity(
                title = "Swiggy Food",
                amount = 250.0,
                type = TransactionType.EXPENSE,
                categoryName = "Food",
                accountId = accountIds[0],
                note = "Dinner order",
                smsId = 123456789L
            ),
            TransactionEntity(
                title = "Salary",
                amount = 80000.0,
                type = TransactionType.INCOME,
                categoryName = "Salary",
                accountId = accountIds[0],
                smsId = 987654321L
            ),
            TransactionEntity(
                title = "Uber Ride",
                amount = 150.0,
                type = TransactionType.EXPENSE,
                categoryName = "Travel",
                accountId = accountIds[1],
                smsId = 555555555L
            ),
            TransactionEntity(
                title = "Amazon Shopping",
                amount = 1200.0,
                type = TransactionType.EXPENSE,
                categoryName = "Shopping",
                accountId = accountIds[0],
                smsId = 111111111L
            ),
            TransactionEntity(
                title = "Freelance Payment",
                amount = 200.0,
                type = TransactionType.INCOME,
                categoryName = "Freelance",
                accountId = accountIds[2],
                smsId = 222222222L
            )
        )

        transactionDao.insertAll(transactions)
    }
}