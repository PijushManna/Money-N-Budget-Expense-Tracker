package com.expense.tracker.core.di

import android.content.Context
import androidx.room.Room
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

}