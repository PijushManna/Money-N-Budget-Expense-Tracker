package com.expense.tracker.feature.recurring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.RecurringFrequency
import com.expense.tracker.core.data.local.entities.RecurringPaymentEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.feature.recurring.ui.RecurringPaymentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecurringPaymentViewModel @Inject constructor(
    private val repository: RecurringPaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecurringPaymentState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun onAmountChange(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun onFrequencyChange(frequency: RecurringFrequency) {
        _uiState.value = _uiState.value.copy(frequency = frequency)
    }

    fun onStartDateChange(date: Long) {
        _uiState.value = _uiState.value.copy(startDate = date)
    }

    fun onCategoryChange(category: Category) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onCategorySheetOpen() {
        _uiState.value = _uiState.value.copy(showCategorySheet = true)
    }

    fun onCategorySheetDismiss() {
        _uiState.value = _uiState.value.copy(showCategorySheet = false)
    }

    fun addRecurringPayment() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.title.isNotBlank() && state.amount.isNotBlank()) {
                repository.insertRecurringPayment(
                    RecurringPaymentEntity(
                        title = state.title,
                        amount = state.amount.toDouble(),
                        type = state.type,
                        cid = state.selectedCategory.id,
                        frequency = state.frequency,
                        startDate = state.startDate
                    )
                )
            }
        }
    }

    fun onActiveChange(isActive: Boolean) {
        _uiState.value = _uiState.value.copy(isActive = isActive)
    }
}
