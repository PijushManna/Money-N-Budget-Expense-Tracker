package com.expense.tracker.core.domain.models

data class AccountUi(
    val id: String,
    val name: String,
    val balance: String,
    val subtitle: String? = null
)
