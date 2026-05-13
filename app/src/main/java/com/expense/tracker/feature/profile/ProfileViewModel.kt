package com.expense.tracker.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.AccountEntity
import com.expense.tracker.core.domain.usecase.AddAccountUseCase
import com.expense.tracker.core.domain.usecase.GetAllAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase,
    private val getAllAccountsUseCase: GetAllAccountsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        getAllAccounts()
    }

    private fun getAllAccounts() {
        viewModelScope.launch {
            getAllAccountsUseCase().collectLatest { accounts ->
                _state.value = _state.value.copy(
                    accounts = accounts,
                    selectedAccount = accounts.firstOrNull()
                )
            }
        }
    }

    fun addAccount(account: AccountEntity) {
        viewModelScope.launch {
            addAccountUseCase(account)
        }
    }

    fun showAddAccountDialog() {
        _state.value = _state.value.copy(isAddAccountDialogVisible = true)
    }

    fun hideAddAccountDialog() {
        _state.value = _state.value.copy(isAddAccountDialogVisible = false)
    }
}
