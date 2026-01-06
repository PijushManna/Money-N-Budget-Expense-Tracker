package com.expense.tracker.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.expense.tracker.core.data.local.convertors.Converters
import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.TransactionDao
import com.expense.tracker.core.data.local.entities.BudgetEntity
import com.expense.tracker.core.data.local.entities.CategoryEntity
import com.expense.tracker.core.data.local.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BudgetDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
}
