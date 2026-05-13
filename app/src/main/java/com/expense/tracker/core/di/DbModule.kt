package com.expense.tracker.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.expense.tracker.core.data.local.dao.AccountDao
import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.GoalDao
import com.expense.tracker.core.data.local.dao.RecurringPaymentDao
import com.expense.tracker.core.data.local.dao.TransactionDao
import com.expense.tracker.core.data.local.db.BudgetDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BudgetDatabase =
        Room.databaseBuilder(
            context, BudgetDatabase::class.java, "budget_db"
        )
            .addMigrations(
                object : Migration(2, 3) {
                    override fun migrate(db: SupportSQLiteDatabase) {
                        db.execSQL(
                            """
                            CREATE TABLE IF NOT EXISTS goals (
                                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                title TEXT NOT NULL,
                                type TEXT NOT NULL,
                                targetAmount REAL NOT NULL,
                                period TEXT NOT NULL,
                                createdAt INTEGER NOT NULL
                            )
                            """.trimIndent()
                        )
                    }
                }
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun bindTransactionDao(db: BudgetDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun bindCategoryDao(db: BudgetDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun bindBudgetDao(db: BudgetDatabase): BudgetDao = db.budgetDao()

    @Provides
    @Singleton
    fun bindRecurringPaymentDao(db: BudgetDatabase): RecurringPaymentDao = db.recurringPaymentDao()

    @Provides
    @Singleton
    fun bindAccountDao(db: BudgetDatabase): AccountDao = db.accountDao()

    @Provides
    @Singleton
    fun bindGoalDao(db: BudgetDatabase): GoalDao = db.goalDao()
}
