package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun addCategory(category: CategoryEntity)
    fun getCategories(): Flow<List<CategoryEntity>>
    fun getCategoryById(id: Long): Flow<CategoryEntity?>
}
