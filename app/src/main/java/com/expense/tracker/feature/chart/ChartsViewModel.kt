package com.expense.tracker.feature.chart

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.CategoryStat
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.feature.chart.use_case.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartsUiState())
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    init {
        combine(
            uiState.map { it.selectedFilter }.distinctUntilChanged(),
            uiState.map { it.selectedDate }.distinctUntilChanged()
        ) { filter, date ->
            filter to date
        }.flatMapLatest { (filter, date) ->
            val (startDate, endDate) = when (filter) {
                "Weekly" -> {
                    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    val endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    startOfWeek to endOfWeek
                }
                "Monthly" -> {
                    val startOfMonth = date.withDayOfMonth(1)
                    val endOfMonth = date.withDayOfMonth(date.lengthOfMonth())
                    startOfMonth to endOfMonth
                }
                "Yearly" -> {
                    val startOfYear = date.withDayOfYear(1)
                    val endOfYear = date.withDayOfYear(date.lengthOfYear())
                    startOfYear to endOfYear
                }
                else -> date to date
            }
            getTransactionsUseCase(startDate, endDate)
        }.onEach { transactions ->
            val expenseTransactions = transactions.filter { it.type == TransactionType.EXPENSE }
            val categoryStats = expenseTransactions.groupBy { it.categoryId }
                .map { (categoryId, transactions) ->
                    val category = expenseCategories[categoryId]
                    CategoryStat(
                        title = category?.label ?: "Unknown",
                        value = transactions.sumOf { it.amount }.toInt(),
                        icon = category?.icon,
                        iconBgColor = Color.White
                    )
                }
            _uiState.update { it.copy(categoryStats = categoryStats) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ChartsEvent) {
        when (event) {
            is ChartsEvent.FilterBy -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }
            }
            is ChartsEvent.ChangeDate -> {
                _uiState.update { it.copy(selectedDate = event.date) }
            }
        }
    }
}

data class ChartsUiState(
    val categoryStats: List<CategoryStat> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFilter: String = "Weekly",
    val selectedDate: LocalDate = LocalDate.now()
)

sealed class ChartsEvent {
    data class FilterBy(val filter: String) : ChartsEvent()
    data class ChangeDate(val date: LocalDate) : ChartsEvent()
}
