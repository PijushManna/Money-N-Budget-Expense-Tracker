package com.expense.tracker.core.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithCurrency(
    @Embedded val account: AccountEntity,

    @Relation(
        parentColumn = "currencyCode",
        entityColumn = "code"
    )
    val currency: CurrencyEntity
)