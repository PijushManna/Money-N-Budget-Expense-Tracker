package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    suspend fun setBudget(budget: BudgetEntity)

    fun getBudgetsForMonth(month: String): Flow<List<BudgetEntity>>
}
