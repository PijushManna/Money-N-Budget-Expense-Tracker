package com.expense.tracker.core.models

import androidx.annotation.Keep
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.SmokingRooms
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.ui.graphics.vector.ImageVector

@Keep
data class Category(
    val label: String,
    val icon: ImageVector
){
    constructor():this("", Icons.Default.Category)
}

val expenseCategories = listOf(
    Category("Shopping", Icons.Outlined.ShoppingCart),
    Category("Food", Icons.Outlined.Restaurant),
    Category("Phone", Icons.Outlined.PhoneAndroid),
    Category("Entertainment", Icons.Outlined.Mic),
    Category("Education", Icons.AutoMirrored.Outlined.MenuBook),
    Category("Beauty", Icons.Outlined.Face),
    Category("Sports", Icons.Outlined.Pool),
    Category("Social", Icons.Outlined.People),
    Category("Transportation", Icons.Outlined.DirectionsBus),
    Category("Clothing", Icons.Outlined.Checkroom),
    Category("Car", Icons.Outlined.DirectionsCar),
    Category("Alcohol", Icons.Outlined.WineBar),
    Category("Cigarettes", Icons.Outlined.SmokingRooms),
    Category("Electronics", Icons.Outlined.Devices),
    Category("Travel", Icons.Outlined.Flight),
    Category("Health", Icons.Outlined.MedicalServices),
    Category("Pets", Icons.Outlined.Pets),
    Category("Repairs", Icons.Outlined.Build),
    Category("Housing", Icons.Outlined.Home),
    Category("Gifts", Icons.Outlined.CardGiftcard),
    Category("Donations", Icons.Outlined.Favorite),
    Category("Lottery", Icons.Outlined.ConfirmationNumber),
    Category("Snacks", Icons.Outlined.Cake),
    Category("Kids", Icons.Outlined.ChildCare),
    Category("Vegetables", Icons.Outlined.Eco),
    Category("Fruits", Icons.Outlined.LocalGroceryStore),
    Category("Settings", Icons.Outlined.Add)
)
