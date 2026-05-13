package com.expense.tracker.core.data.local.dao

import androidx.room.Dao
import com.expense.tracker.core.data.local.entities.CurrencyEntity

@Dao
interface CurrencyDao : BaseDao<CurrencyEntity>