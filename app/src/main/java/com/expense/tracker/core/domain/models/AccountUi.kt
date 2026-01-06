package com.expense.tracker.core.domain.models

import androidx.annotation.Keep

@Keep
data class AccountUi(
    val id: String,
    val name: String,
    val balance: String,
    val subtitle: String? = null
)
