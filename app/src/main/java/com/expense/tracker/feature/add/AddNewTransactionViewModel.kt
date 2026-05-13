package com.expense.tracker.feature.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.tracker.core.data.local.entities.AccountWithCurrency
import com.expense.tracker.core.data.local.entities.TransactionEntity
import com.expense.tracker.core.data.local.entities.TransactionType
import com.expense.tracker.core.domain.models.Category
import com.expense.tracker.core.domain.models.expenseCategories
import com.expense.tracker.core.domain.models.incomeCategories
import com.expense.tracker.core.domain.repo.TransactionRepository
import com.expense.tracker.core.domain.usecase.AddTransactionUseCase
import com.expense.tracker.core.domain.usecase.GetAllAccountsUseCase
import com.expense.tracker.utils.formatAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

data class AddNewTransactionUiState(
    val id: Long = -1,
    val tabs: List<String> = listOf("Income", "Expenses", "Transaction"),
    val selectedTabIndex: Int = 1,
    val showNumpad: Boolean = false,
    val categories: List<Category> = expenseCategories.values.toList(),
    val selectedCategory: Category = Category(),
    val amount: String = "0",
    val title: String = "",
    val note: String = "",
    val selectedDateMillis: Long = System.currentTimeMillis(),
    val accounts: List<AccountWithCurrency> = emptyList(),
    val selectedAccount: AccountWithCurrency? = null,
    val isAccountSelectionDialogVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false
)

@HiltViewModel
class AddNewTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(AddNewTransactionUiState())
        private set

    private var transactionAccountId: Long? = null

    init {
        viewModelScope.launch {
            getAllAccountsUseCase().collectLatest { accounts ->
                val selectedAccountId = uiState.selectedAccount?.account?.id ?: transactionAccountId
                uiState = uiState.copy(
                    accounts = accounts,
                    selectedAccount = accounts.firstOrNull { it.account.id == selectedAccountId }
                        ?: accounts.firstOrNull()
                )
            }
        }

        savedStateHandle.get<Long>("id")?.let { id ->
            if (id >= 0) {
                viewModelScope.launch {
                    transactionRepository.getTransactionById(id).collectLatest { transaction ->
                        if (transaction == null) return@collectLatest

                        transactionAccountId = transaction.accountId
                        uiState = uiState.copy(
                            id = transaction.id,
                            selectedTabIndex = if (transaction.type == TransactionType.INCOME) 0 else 1,
                            selectedCategory = Category(),
                            amount = transaction.amount.formatAmount(),
                            title = transaction.title.ifBlank { transaction.defaultTitle() },
                            note = transaction.note ?: "",
                            selectedDateMillis = transaction.timestamp,
                            showNumpad = true,
                            selectedAccount = uiState.accounts.find { it.account.id == transaction.accountId }
                        )
                    }
                }
            }
        }
    }

    fun onTabSelected(index: Int) {
        val newCategories = when(index) {
            0 -> incomeCategories.values.toList()
            1 -> expenseCategories.values.toList()
            else -> emptyList()
        }
        uiState = uiState.copy(
            selectedTabIndex = index,
            categories = newCategories,
            selectedCategory = Category(),
            showNumpad = false,
            amount = "0",
            title = ""
        )
    }

    fun onCategorySelected(category: Category) {
        uiState = uiState.copy(
            selectedCategory = category,
            showNumpad = category.label.isNotBlank(),
            title = category.defaultTitle()
        )
    }

    fun onKeyPress(key: String) {
        when (key) {
            "✓" -> saveTransaction()
            "Today" -> showDatePicker()
            else -> uiState = uiState.copy(amount = handleInput(uiState.amount, key))
        }
    }

    fun onNoteChange(note: String) {
        uiState = uiState.copy(note = note)
    }

    fun onTitleChange(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun onAccountSelected(account: AccountWithCurrency) {
        uiState = uiState.copy(
            selectedAccount = account,
            isAccountSelectionDialogVisible = false
        )
    }

    fun showAccountSelectionDialog() {
        uiState = uiState.copy(isAccountSelectionDialogVisible = true)
    }

    fun hideAccountSelectionDialog() {
        uiState = uiState.copy(isAccountSelectionDialogVisible = false)
    }

    fun showDatePicker() {
        uiState = uiState.copy(isDatePickerVisible = true)
    }

    fun hideDatePicker() {
        uiState = uiState.copy(isDatePickerVisible = false)
    }

    fun onDateSelected(dateMillis: Long) {
        uiState = uiState.copy(
            selectedDateMillis = dateMillis.toLocalStartOfDayMillis(),
            isDatePickerVisible = false
        )
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            val selectedAccountId = uiState.selectedAccount?.account?.id ?: return@launch
            val title = uiState.title.ifBlank { uiState.selectedCategory.defaultTitle() }
            var transaction = TransactionEntity(
                title = title,
                categoryName = uiState.selectedCategory.label,
                amount = uiState.amount.toDouble(),
                type = if (uiState.selectedTabIndex == 0) TransactionType.INCOME else TransactionType.EXPENSE,
                note = uiState.note,
                accountId = selectedAccountId,
                timestamp = uiState.selectedDateMillis,
                smsId = System.currentTimeMillis()
            )
            if (uiState.id != -1L) transaction = transaction.copy(id = uiState.id)
            addTransactionUseCase(transaction)
            uiState = uiState.copy(
                showNumpad = false,
                selectedCategory = Category(),
                amount = "0",
                title = "",
                note = "",
                selectedDateMillis = System.currentTimeMillis()
            )
        }
    }

    private fun handleInput(current: String, key: String): String {
        return when (key) {
            "⌫" -> if (current.length > 1) current.dropLast(1) else "0"
            "+", "-", "Today" -> current
            else -> if (current == "0") key else (current + key)
        }
    }

    private fun Long.toLocalStartOfDayMillis(): Long {
        val selectedDate = Instant.ofEpochMilli(this)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()

        return selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    private fun Category.defaultTitle(): String {
        if (label.isBlank()) return ""
        val type = if (uiState.selectedTabIndex == 0) "Income" else "Expense"
        return "$label $type"
    }

    private fun TransactionEntity.defaultTitle(): String {
        val typeLabel = if (type == TransactionType.INCOME) "Income" else "Expense"
        return if (categoryName.isBlank()) typeLabel else "$categoryName $typeLabel"
    }
}
