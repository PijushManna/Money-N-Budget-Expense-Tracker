package com.expense.tracker.core.data.local.entities

import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.expense.tracker.utils.formatAmount

@Keep
data class TransactionWithAccount(
    @Embedded val transaction: TransactionEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "code",
        entity = CurrencyEntity::class,
        associateBy = Junction(
            value = AccountEntity::class,
            parentColumn = "id",
            entityColumn = "currencyCode"
        )
    )
    val currency: CurrencyEntity
){
    val amount: String
        get() = transaction.amount.formatAmount(currency.symbol)
}