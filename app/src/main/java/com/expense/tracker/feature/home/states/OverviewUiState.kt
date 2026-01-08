package com.expense.tracker.feature.home.states

data class OverviewUiState(
    val selectedYear: String,
    val selectedMonth: String,
    val totalIncome: String,
    val totalExpense: String,
    val totalBalance: String,
    val totalBalanceCalculation: String
){
    constructor():this("","","","","","")
}