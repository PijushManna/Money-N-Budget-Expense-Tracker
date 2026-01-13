package com.expense.tracker.core.data.mapper

import android.R.attr.category
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AttachMoney
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
import androidx.compose.material.icons.outlined.Gif
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.MonetizationOn
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

object CategoryIconMapper {
    private val mapper:Map<String, ImageVector> by lazy {
        hashMapOf(
            "Salary" to Icons.Outlined.Home,
            "Business" to Icons.Outlined.Build,
            "Gift" to Icons.Outlined.CardGiftcard,
            "Lottery" to Icons.Outlined.ConfirmationNumber,
            "Other" to Icons.Outlined.Money,
            "Recurring" to Icons.Outlined.Sync,
            "Shopping" to Icons.Outlined.ShoppingCart,
            "Food" to Icons.Outlined.Restaurant,
            "Phone" to Icons.Outlined.PhoneAndroid,
            "Entertainment" to Icons.Outlined.Mic,
            "Education" to Icons.AutoMirrored.Outlined.MenuBook,
            "Beauty" to Icons.Outlined.Face,
            "Sports" to Icons.Outlined.Pool,
            "Social" to Icons.Outlined.People,
            "Transportation" to Icons.Outlined.DirectionsBus,
            "Clothing" to Icons.Outlined.Checkroom,
            "Car" to Icons.Outlined.DirectionsCar,
            "Alcohol" to Icons.Outlined.WineBar,
            "Cigarettes" to Icons.Outlined.SmokingRooms,
            "Electronics" to Icons.Outlined.Devices,
            "Travel" to Icons.Outlined.Flight,
            "Health" to Icons.Outlined.MedicalServices,
            "Pets" to Icons.Outlined.Pets,
            "Repairs" to Icons.Outlined.Build,
            "Housing" to Icons.Outlined.Home,
            "Gifts" to Icons.Outlined.CardGiftcard,
            "Donations" to Icons.Outlined.Favorite,
            "Snacks" to Icons.Outlined.Cake,
            "Kids" to Icons.Outlined.ChildCare,
            "Vegetables" to Icons.Outlined.Eco,
            "Fruits" to Icons.Outlined.LocalGroceryStore,
            "Others" to Icons.Outlined.Interests
        )
    }

    fun getCategoryIconFromName(categoryName:String): ImageVector {
        return mapper[categoryName] ?: Icons.Outlined.AttachMoney
    }
}