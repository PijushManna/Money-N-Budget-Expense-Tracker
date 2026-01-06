package com.expense.tracker.feature.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.CandlestickChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun Footer(modifier: Modifier = Modifier, currentRoute: String, onRouteChange: (String) -> Unit = {}) {

    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                bottomNavigationItems.forEachIndexed { index, it ->
                    if (index == bottomNavigationItems.size/ 2){
                        FloatingActionButton(onClick = {
                            onRouteChange("add")
                        }, containerColor = MaterialTheme.colorScheme.primary){
                            Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                        }
                    }
                    Column {
                        val tint = if (it.route == currentRoute) MaterialTheme.colorScheme.primary else LocalContentColor.current
                        IconButton(onClick = {
                            onRouteChange(it.route)
                        }) {
                            Icon(
                                imageVector = if(it.route == currentRoute) it.selectedIcon else it.defaultIcon,
                                contentDescription = it.label,
                                tint = tint
                            )
                        }
                        Text(it.label, color = tint)
                    }
                }
            }
        },
    )
}

val bottomNavigationItems: List<BottomNavigationRoute> = listOf(
    BottomNavigationRoute.Home,
    BottomNavigationRoute.Charts,
    BottomNavigationRoute.Reports,
    BottomNavigationRoute.Profile
)

sealed class BottomNavigationRoute(
    val route: String,
    val defaultIcon: ImageVector,
    val selectedIcon: ImageVector,
    val label: String
) {
    object Home : BottomNavigationRoute("home", Icons.Outlined.Home, Icons.Filled.Home, "Home")
    object Charts :
        BottomNavigationRoute("charts", Icons.Outlined.CandlestickChart, Icons.Filled.CandlestickChart, "Charts")

    object Reports :
        BottomNavigationRoute("reports", Icons.Outlined.Receipt, Icons.Filled.Receipt, "Reports")

    object Profile : BottomNavigationRoute(
        "profile",
        Icons.Outlined.Person,
        Icons.Filled.Person,
        "Profile"
    )
}