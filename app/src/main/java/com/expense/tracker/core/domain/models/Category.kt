package com.expense.tracker.core.domain.models

import androidx.annotation.Keep
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.SmokingRooms
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.ui.graphics.vector.ImageVector

@Keep
data class Category(
    val id: Long = 0L,
    val label: String,
    val icon: ImageVector
){
    constructor():this(0L, "", Icons.Default.Category)
}

val incomeCategories = listOf(
    Category(101, "Salary", Icons.Outlined.Home),
    Category(102, "Business", Icons.Outlined.Build),
    Category(103, "Gift", Icons.Outlined.CardGiftcard),
    Category(105, "Lottery", Icons.Outlined.ConfirmationNumber),
    Category(104, "Other", Icons.Outlined.Money),
    Category(106, "Recurring", Icons.Outlined.Sync),
).associateBy(Category::id)

val expenseCategories = listOf(
    Category(1, "Shopping", Icons.Outlined.ShoppingCart),
    Category(2, "Food", Icons.Outlined.Restaurant),
    Category(3, "Phone", Icons.Outlined.PhoneAndroid),
    Category(4, "Entertainment", Icons.Outlined.Mic),
    Category(5, "Education", Icons.AutoMirrored.Outlined.MenuBook),
    Category(6, "Beauty", Icons.Outlined.Face),
    Category(7, "Sports", Icons.Outlined.Pool),
    Category(8, "Social", Icons.Outlined.People),
    Category(9, "Transportation", Icons.Outlined.DirectionsBus),
    Category(10, "Clothing", Icons.Outlined.Checkroom),
    Category(11, "Car", Icons.Outlined.DirectionsCar),
    Category(12, "Alcohol", Icons.Outlined.WineBar),
    Category(13, "Cigarettes", Icons.Outlined.SmokingRooms),
    Category(14, "Electronics", Icons.Outlined.Devices),
    Category(15, "Travel", Icons.Outlined.Flight),
    Category(16, "Health", Icons.Outlined.MedicalServices),
    Category(17, "Pets", Icons.Outlined.Pets),
    Category(18, "Repairs", Icons.Outlined.Build),
    Category(19, "Housing", Icons.Outlined.Home),
    Category(20, "Gifts", Icons.Outlined.CardGiftcard),
    Category(21, "Donations", Icons.Outlined.Favorite),
    Category(22, "Lottery", Icons.Outlined.ConfirmationNumber),
    Category(23, "Snacks", Icons.Outlined.Cake),
    Category(24, "Kids", Icons.Outlined.ChildCare),
    Category(25, "Vegetables", Icons.Outlined.Eco),
    Category(26, "Fruits", Icons.Outlined.LocalGroceryStore),
    Category(27, "Others", Icons.Outlined.Interests),
    Category(106, "Recurring", Icons.Outlined.Sync),
).associateBy(Category::id)
