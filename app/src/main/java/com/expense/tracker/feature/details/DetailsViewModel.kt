package com.expense.tracker.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.domain.repo.CategoryRepository
import com.expense.tracker.core.domain.repo.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

//    init {
//        savedStateHandle.get<Long>("id")?.let { transactionId ->
//            if (transactionId != -1L) {
//                viewModelScope.launch {
//                    transactionRepository.getTransactionById(transactionId)
//                        .flatMapLatest { transaction ->
//                            if (transaction == null) {
//                                return@flatMapLatest flowOf(null to null)
//                            }
//                            categoryRepository.getCategoryById(transaction.categoryId)
//                                .map { category ->
//                                    transaction to category
//                                }
//                        }.collectLatest { (transaction, category) ->
//                            if (transaction != null && category != null) {
//                                _state.value = DetailsState(
//                                    id = transaction.id,
//                                    title = transaction.title,
//                                    amount = "%.2f".format(transaction.amount),
//                                    type = transaction.type,
//                                    category = category.name,
//                                    date = SimpleDateFormat(
//                                        "dd MMM yyyy, hh:mm a",
//                                        Locale.getDefault()
//                                    ).format(transaction.timestamp),
//                                    note = transaction.note ?: "",
//                                    currency = transaction.currency
//                                )
//                            }
//                        }
//                }
//            }
//        }
//    }

    fun deleteTransaction() {
        viewModelScope.launch {
            if (state.value.id != -1L) {
                transactionRepository.deleteById(state.value.id)
            }
        }
    }
}
