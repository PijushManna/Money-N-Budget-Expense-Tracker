package com.expense.tracker.feature.profile

import com.expense.tracker.core.data.local.entities.AccountWithCurrency

data class ProfileState(
    val accounts: List<AccountWithCurrency> = emptyList(),
    val selectedAccount: AccountWithCurrency? = null,
    val isAddAccountDialogVisible: Boolean = false
)
