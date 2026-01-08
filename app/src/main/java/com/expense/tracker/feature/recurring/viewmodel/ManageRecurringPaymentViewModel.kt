package com.expense.tracker.feature.recurring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.domain.repo.RecurringPaymentRepository
import com.expense.tracker.feature.recurring.RecurringPaymentUi
import com.expense.tracker.utils.formatAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ManageRecurringPaymentViewModel @Inject constructor(
    private val repository: RecurringPaymentRepository
): ViewModel() {

    val allPayments = repository.getAllRecurringPayments().map { items ->
        items.map {
            RecurringPaymentUi(
                id = it.id,
                title = it.title,
                amountText = it.amount.formatAmount(),
                frequencyLabel = it.frequency.name.lowercase().replaceFirstChar {c -> c.uppercase() },
                isActive = it.isActive
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun updateRecurringPayment(id: Long, active: Boolean) {
        viewModelScope.launch {
            val payment = repository.getRecurringPaymentById(id)
            if (payment != null) {
                repository.updateRecurringPayment(payment.copy(isActive = active))
            }
        }
    }

    fun deleteRecurringPayment(id: Long) {
        viewModelScope.launch {
            repository.deleteRecurringPayment(id)
        }
    }
}