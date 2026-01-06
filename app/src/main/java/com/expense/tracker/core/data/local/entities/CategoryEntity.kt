package com.expense.tracker.core.data.local.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val icon: String, // emoji or icon name
    val color: Long  // Color hex stored as Long
)
