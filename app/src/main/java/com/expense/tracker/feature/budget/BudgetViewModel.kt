package com.expense.tracker.feature.budget

import android.os.Build
import androidx.lifecycle.ViewModel
import com.expense.tracker.core.domain.repo.BudgetRepository
import com.expense.tracker.core.domain.repo.CategoryRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    }
}
