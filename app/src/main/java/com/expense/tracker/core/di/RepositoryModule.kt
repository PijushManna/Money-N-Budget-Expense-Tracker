package com.expense.tracker.core.di

import com.expense.tracker.core.data.repo.AccountRepositoryImpl
import com.expense.tracker.core.data.repo.CategoryRepositoryImpl
import com.expense.tracker.core.data.repo.RecurringPaymentRepositoryImpl
import com.expense.tracker.core.data.repo.SmsRepoImpl
import com.expense.tracker.core.data.repo.TransactionRepositoryImpl
import com.expense.tracker.core.domain.repo.AccountRepository
import com.expense.tracker.core.domain.repo.CategoryRepository
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.core.domain.repo.SmsRepo
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.core.domain.usecase.AddSmsTransactionsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    abstract fun bindRecurringPaymentRepository(impl: RecurringPaymentRepositoryImpl): RecurringPaymentRepository

    @Binds
    abstract fun bindSmsRepo(impl: SmsRepoImpl): SmsRepo
}