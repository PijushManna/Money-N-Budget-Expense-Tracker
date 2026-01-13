package com.expense.tracker.feature.budget

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.BudgetEntity
import com.expense.tracker.core.domain.repo.BudgetRepository
import com.expense.tracker.core.domain.repo.CategoryRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.YearMonth

class BudgetViewModel(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState = _uiState.asStateFlow()

    private val currentMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        YearMonth.now().toString()
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    init {
        observeBudgets()
    }

    private fun observeBudgets() {
        viewModelScope.launch {
            combine(
                budgetRepository.getBudgetsForMonth(currentMonth),
                categoryRepository.getAllCategories(),
                transactionRepository.getCategoryWiseExpense(currentMonth)
            ) { budgets, categories, spends ->

                val spendMap = spends.associateBy { it.categoryId }

                BudgetUiState(
                    categoryBudgets = budgets,
                    categories = categories,
                    categorySpends = spendMap
                )
            }.collect {
                _uiState.value = it
            }
        }
    }


    fun setCategoryBudget(categoryId: Long, amount: Double) {
        viewModelScope.launch {
            budgetRepository.setBudget(
                BudgetEntity(
                    categoryId = categoryId,
                    limitAmount = amount,
                    month = currentMonth
                )
            )
        }
    }
}
