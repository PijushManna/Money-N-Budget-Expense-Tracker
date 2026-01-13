package com.expense.tracker.core.domain.models

data class SmsMessage(
    val id:Long,
    val address: String,
    val body: String,
    val date: Long
)