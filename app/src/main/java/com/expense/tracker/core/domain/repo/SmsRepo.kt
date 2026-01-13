package com.expense.tracker.core.domain.repo

import com.expense.tracker.core.domain.models.SmsMessage


interface SmsRepo {
    fun getAllSms(): List<SmsMessage>
}