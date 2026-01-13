package com.expense.tracker.feature.chart

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.CategoryStat
import com.expense.tracker.feature.chart.use_case.GetTransactionsUseCase
import com.expense.tracker.utils.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartsUiState())
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    init {
        combine(
            uiState.map { it.selectedFilter }.distinctUntilChanged(),
            uiState.map { it.selectedDate }.distinctUntilChanged(),
            uiState.map { it.transactionType }.distinctUntilChanged()
        ) { filter, date, type ->
            Triple(filter, date, type)
        }.flatMapLatest { (filter, date, type) ->
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
            getTransactionsUseCase(startDate.toLong(), endDate.toLong())
        }.onEach { transactions ->
            val filteredTransactions = transactions.filter { it.type == uiState.value.transactionType }
            val currency = transactions.first().currency
            val categoryStats = filteredTransactions.groupBy { it.categoryName }
                .map { (categoryId, transactions) ->
                    val category = if(transactions.first().type == TransactionType.INCOME)
                        Category()
                    else
                        Category()

                    CategoryStat(
                        title = category?.label ?: "Unknown",
                        value = transactions.sumOf { it.amount },
                        icon = category?.icon ?: Icons.Default.Report,
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
            is ChartsEvent.ChangeTransactionType -> {
                _uiState.update { it.copy(transactionType = event.type) }
            }
        }
    }
}

data class ChartsUiState(
    val categoryStats: List<CategoryStat> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFilter: String = "Weekly",
    val selectedDate: LocalDate = LocalDate.now(),
    val transactionType: TransactionType = TransactionType.EXPENSE
)

sealed class ChartsEvent {
    data class FilterBy(val filter: String) : ChartsEvent()
    data class ChangeDate(val date: LocalDate) : ChartsEvent()
    data class ChangeTransactionType(val type: TransactionType) : ChartsEvent()
}
