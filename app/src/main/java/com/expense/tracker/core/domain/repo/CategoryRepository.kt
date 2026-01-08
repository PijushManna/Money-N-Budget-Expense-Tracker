package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.data.local.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategories(): Flow<List<CategoryEntity>>

    fun getCategoryById(id: Long): Flow<CategoryEntity?>
}
