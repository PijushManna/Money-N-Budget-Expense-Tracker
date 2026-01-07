package com.expense.tracker.core.di

import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.dao.RecurringPaymentDao
import com.expense.tracker.core.data.local.dao.TransactionDao
import com.expense.tracker.core.data.repo.BudgetRepositoryImpl
import com.expense.tracker.core.data.repo.CategoryRepositoryImpl
import com.expense.tracker.core.data.repo.RecurringPaymentRepositoryImpl
import com.expense.tracker.core.data.repo.TransactionRepositoryImpl
import com.expense.tracker.core.domain.repo.BudgetRepository
import com.expense.tracker.core.domain.repo.CategoryRepository
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideTransactionRepository(
        dao: TransactionDao
    ): TransactionRepository = TransactionRepositoryImpl(dao)

    @Provides
    fun provideCategoryRepository(
        dao: CategoryDao
    ): CategoryRepository = CategoryRepositoryImpl(dao)

    @Provides
    fun provideBudgetRepository(
        dao: BudgetDao
    ): BudgetRepository = BudgetRepositoryImpl(dao)

    @Provides
    fun provideRecurringPaymentRepository(
        dao: RecurringPaymentDao
    ): RecurringPaymentRepository = RecurringPaymentRepositoryImpl(dao)
}
