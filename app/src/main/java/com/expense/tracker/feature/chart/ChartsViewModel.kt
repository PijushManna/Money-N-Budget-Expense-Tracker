package com.expense.tracker.feature.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.data.mapper.CategoryIconMapper
import com.expense.tracker.core.domain.models.CategoryStat
import com.expense.tracker.feature.chart.use_case.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
            ChartSelection(filter, date, type)
        }.flatMapLatest { selection ->
            val range = selection.toDateRange()
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    periodLabel = range.label
                )
            }

            getTransactionsUseCase(range.startMillis, range.endMillis)
                .map { transactions -> selection to transactions }
        }.onEach { (selection, transactions) ->
            val filteredTransactions = transactions
                .map { it.transaction }
                .filter { it.type == selection.type }

            val categoryStats = filteredTransactions
                .groupBy { it.categoryName.ifBlank { "Uncategorized" } }
                .map { (categoryName, items) ->
                    CategoryStat(
                        title = categoryName,
                        value = items.sumOf { it.amount },
                        icon = CategoryIconMapper.getCategoryIconFromName(categoryName)
                    )
                }
                .sortedByDescending { it.value }

            _uiState.update {
                it.copy(
                    categoryStats = categoryStats,
                    totalAmount = filteredTransactions.sumOf { transaction -> transaction.amount },
                    transactionCount = filteredTransactions.size,
                    isLoading = false,
                    error = null
                )
            }
        }.catch { throwable ->
            _uiState.update {
                it.copy(
                    categoryStats = emptyList(),
                    totalAmount = 0.0,
                    transactionCount = 0,
                    isLoading = false,
                    error = throwable.message ?: "Unable to load chart data"
                )
            }
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

            ChartsEvent.NextPeriod -> {
                _uiState.update { it.copy(selectedDate = it.selectedDate.shiftBy(it.selectedFilter, 1)) }
            }

            ChartsEvent.PreviousPeriod -> {
                _uiState.update { it.copy(selectedDate = it.selectedDate.shiftBy(it.selectedFilter, -1)) }
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
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val periodLabel: String = "",
    val totalAmount: Double = 0.0,
    val transactionCount: Int = 0
)

sealed class ChartsEvent {
    data class FilterBy(val filter: String) : ChartsEvent()
    data class ChangeDate(val date: LocalDate) : ChartsEvent()
    data class ChangeTransactionType(val type: TransactionType) : ChartsEvent()
    data object PreviousPeriod : ChartsEvent()
    data object NextPeriod : ChartsEvent()
}

private data class ChartSelection(
    val filter: String,
    val date: LocalDate,
    val type: TransactionType
)

private data class ChartDateRange(
    val startMillis: Long,
    val endMillis: Long,
    val label: String
)

private fun ChartSelection.toDateRange(): ChartDateRange {
    val startDate = when (filter) {
        "Weekly" -> date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        "Monthly" -> date.withDayOfMonth(1)
        "Yearly" -> date.withDayOfYear(1)
        else -> date
    }

    val endDate = when (filter) {
        "Weekly" -> date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        "Monthly" -> date.withDayOfMonth(date.lengthOfMonth())
        "Yearly" -> date.withDayOfYear(date.lengthOfYear())
        else -> date
    }

    return ChartDateRange(
        startMillis = startDate.toStartOfDayMillis(),
        endMillis = endDate.toEndOfDayMillis(),
        label = when (filter) {
            "Weekly" -> "${startDate.format(dayMonthFormatter)} - ${endDate.format(dayMonthFormatter)}"
            "Monthly" -> date.format(monthFormatter)
            "Yearly" -> date.year.toString()
            else -> date.format(dayMonthFormatter)
        }
    )
}

private fun LocalDate.shiftBy(filter: String, amount: Long): LocalDate {
    return when (filter) {
        "Weekly" -> plusWeeks(amount)
        "Monthly" -> plusMonths(amount)
        "Yearly" -> plusYears(amount)
        else -> plusDays(amount)
    }
}

private fun LocalDate.toStartOfDayMillis(): Long =
    atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

private fun LocalDate.toEndOfDayMillis(): Long =
    atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

private val dayMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM")
private val monthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
