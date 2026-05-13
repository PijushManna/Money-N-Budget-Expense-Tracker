package com.expense.tracker.feature.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.GoalEntity
import com.expense.tracker.core.data.local.entities.GoalPeriod
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.repo.GoalRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.utils.formatAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    transactionRepository: TransactionRepository,
    private val goalNotifier: GoalNotifier
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()
    private val notifiedGoalStates = mutableSetOf<String>()

    init {
        viewModelScope.launch {
            combine(
                goalRepository.getGoals(),
                transactionRepository.getAllTransactions()
            ) { goals, transactions ->
                val transactionEntities = transactions.map { it.transaction }
                goals.map { goal -> goal.toProgress(transactionEntities) }
            }.collect { goals ->
                goals.forEach(::notifyIfNeeded)
                _uiState.update { it.copy(goals = goals) }
            }
        }
    }

    fun showAddGoalDialog() {
        _uiState.update { it.copy(showAddGoalDialog = true) }
    }

    fun hideAddGoalDialog() {
        _uiState.update { it.copy(showAddGoalDialog = false) }
    }

    fun addGoal(title: String, type: TransactionType, targetAmount: Double, period: GoalPeriod) {
        if (title.isBlank() || targetAmount <= 0.0) return

        viewModelScope.launch {
            goalRepository.upsertGoal(
                GoalEntity(
                    title = title.trim(),
                    type = type,
                    targetAmount = targetAmount,
                    period = period
                )
            )
            hideAddGoalDialog()
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            goalRepository.deleteGoal(goal)
        }
    }

    private fun notifyIfNeeded(goal: GoalProgressUi) {
        val reached = when (goal.goal.type) {
            TransactionType.EXPENSE -> goal.currentAmount > goal.goal.targetAmount
            TransactionType.INCOME -> goal.currentAmount >= goal.goal.targetAmount
        }
        if (!reached) return

        val key = "${goal.goal.id}:${goal.periodLabel}:${goal.goal.type}"
        if (!notifiedGoalStates.add(key)) return

        val (title, message) = when (goal.goal.type) {
            TransactionType.EXPENSE -> "Expense budget exceeded" to
                    "${goal.goal.title} crossed the ${goal.goal.targetAmountLabel()} target for ${goal.periodLabel}."

            TransactionType.INCOME -> "Income milestone achieved" to
                    "${goal.goal.title} reached the ${goal.goal.targetAmountLabel()} target for ${goal.periodLabel}."
        }

        goalNotifier.showGoalNotification(goal.goal.id, title, message)
    }
}

data class GoalsUiState(
    val goals: List<GoalProgressUi> = emptyList(),
    val showAddGoalDialog: Boolean = false
)

data class GoalProgressUi(
    val goal: GoalEntity,
    val currentAmount: Double,
    val progress: Float,
    val remainingAmount: Double,
    val periodLabel: String,
    val status: GoalStatus
)

enum class GoalStatus {
    ON_TRACK, EXCEEDED, ACHIEVED
}

private fun GoalEntity.toProgress(transactions: List<TransactionEntity>): GoalProgressUi {
    val range = period.currentRange()
    val current = transactions
        .filter { it.type == type }
        .filter { it.timestamp in range.startMillis..range.endMillis }
        .sumOf { it.amount }

    val progress = (current / targetAmount).toFloat()
    val status = when {
        type == TransactionType.EXPENSE && current > targetAmount -> GoalStatus.EXCEEDED
        type == TransactionType.INCOME && current >= targetAmount -> GoalStatus.ACHIEVED
        else -> GoalStatus.ON_TRACK
    }

    return GoalProgressUi(
        goal = this,
        currentAmount = current,
        progress = min(progress, 1f),
        remainingAmount = targetAmount - current,
        periodLabel = range.label,
        status = status
    )
}

private data class GoalDateRange(
    val startMillis: Long,
    val endMillis: Long,
    val label: String
)

private fun GoalPeriod.currentRange(): GoalDateRange {
    val today = LocalDate.now()
    val startDate = when (this) {
        GoalPeriod.WEEKLY -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        GoalPeriod.MONTHLY -> today.withDayOfMonth(1)
        GoalPeriod.YEARLY -> today.withDayOfYear(1)
    }
    val endDate = when (this) {
        GoalPeriod.WEEKLY -> today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        GoalPeriod.MONTHLY -> today.withDayOfMonth(today.lengthOfMonth())
        GoalPeriod.YEARLY -> today.withDayOfYear(today.lengthOfYear())
    }

    return GoalDateRange(
        startMillis = startDate.toStartOfDayMillis(),
        endMillis = endDate.toEndOfDayMillis(),
        label = when (this) {
            GoalPeriod.WEEKLY -> "${startDate.format(dayMonthFormatter)} - ${endDate.format(dayMonthFormatter)}"
            GoalPeriod.MONTHLY -> today.format(monthFormatter)
            GoalPeriod.YEARLY -> today.year.toString()
        }
    )
}

private fun LocalDate.toStartOfDayMillis(): Long =
    atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

private fun LocalDate.toEndOfDayMillis(): Long =
    atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

private fun GoalEntity.targetAmountLabel(): String = targetAmount.formatAmount()

private val dayMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM")
private val monthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
