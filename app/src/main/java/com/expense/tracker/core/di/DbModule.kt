package com.expense.tracker.core.di

import android.content.Context
import androidx.room.Room
import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.dao.CategoryDao
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
        ).build()

    @Provides
    @Singleton
    fun bindTransactionDao(db: BudgetDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun bindCategoryDao(db: BudgetDatabase): CategoryDao = db.categoryDao()

    @Provides
    @Singleton
    fun bindBudgetDao(db: BudgetDatabase): BudgetDao = db.budgetDao()
}