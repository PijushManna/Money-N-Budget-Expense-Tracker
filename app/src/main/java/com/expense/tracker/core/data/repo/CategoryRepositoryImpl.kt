package com.expense.tracker.core.data.repo

import com.expense.tracker.core.data.local.dao.CategoryDao
import com.expense.tracker.core.data.local.entities.CategoryEntity
import com.expense.tracker.core.domain.repo.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    override fun getCategoryById(id: Long): Flow<CategoryEntity?> = categoryDao.getCategoryById(id)
}
