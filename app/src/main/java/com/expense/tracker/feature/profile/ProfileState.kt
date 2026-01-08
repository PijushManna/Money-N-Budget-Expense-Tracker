package com.expense.tracker.feature.profile

import com.expense.tracker.core.data.local.entities.AccountEntity

data class ProfileState(
    val accounts: List<AccountEntity> = emptyList(),
    val selectedAccount: AccountEntity? = null,
    val isAddAccountDialogVisible: Boolean = false
)
