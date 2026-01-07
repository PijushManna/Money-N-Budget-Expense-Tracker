package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.entities.CategoryEntity
import com.expense.tracker.core.domain.repo.CategoryRepository
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun addCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    override fun getCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    override fun getCategoryById(id: Long): Flow<CategoryEntity?> {
        return categoryDao.getCategoryById(id)
    }
}
