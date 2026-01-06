package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.BudgetDao
import com.expense.tracker.core.data.local.entities.BudgetEntity
import com.expense.tracker.core.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override suspend fun setBudget(budget: BudgetEntity) {
        budgetDao.insert(budget)
    }

    override fun getBudgetsForMonth(month: String): Flow<List<BudgetEntity>> {
        return budgetDao.getBudgetsForMonth(month)
    }
}
