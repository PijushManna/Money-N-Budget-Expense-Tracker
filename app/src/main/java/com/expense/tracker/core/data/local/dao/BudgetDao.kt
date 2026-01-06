package com.expense.tracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.expense.tracker.core.data.local.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity)

    @Query("""
        SELECT * FROM budgets 
        WHERE month = :month
    """)
    fun getBudgetsForMonth(month: String): Flow<List<BudgetEntity>>
}
