package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    suspend fun upsertGoal(goal: GoalEntity)
    suspend fun deleteGoal(goal: GoalEntity)
    fun getGoals(): Flow<List<GoalEntity>>
}
