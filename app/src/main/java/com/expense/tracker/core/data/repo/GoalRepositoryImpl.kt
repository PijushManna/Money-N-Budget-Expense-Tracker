package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.GoalDao
import com.expense.tracker.core.data.local.entities.GoalEntity
import com.expense.tracker.core.domain.repo.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {
    override suspend fun upsertGoal(goal: GoalEntity) {
        goalDao.upsert(goal)
    }

    override suspend fun deleteGoal(goal: GoalEntity) {
        goalDao.delete(goal)
    }

    override fun getGoals(): Flow<List<GoalEntity>> = goalDao.getGoals()
}
