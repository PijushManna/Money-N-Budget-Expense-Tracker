package com.expense.tracker.feature.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun Footer(modifier: Modifier = Modifier, currentRoute: String) {

    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                bottomNavigationItems.forEachIndexed { index, it ->
                    if (index == bottomNavigationItems.size/ 2){
                        FloatingActionButton(onClick = {}, containerColor = MaterialTheme.colorScheme.primary){
                            Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                        }
                    }
                    Column {
                        val tint by animateColorAsState(if (it.route == currentRoute) MaterialTheme.colorScheme.primary else Color.Unspecified)
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = it.defaultIcon,
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
    BottomNavigationRoute.Settings
)

sealed class BottomNavigationRoute(
    val route: String,
    val defaultIcon: ImageVector,
    val selectedIcon: ImageVector,
    val label: String
) {
    object Home : BottomNavigationRoute("home", Icons.Outlined.Home, Icons.Filled.Home, "Home")
    object Charts :
        BottomNavigationRoute("charts", Icons.Outlined.BarChart, Icons.Filled.BarChart, "Charts")

    object Reports :
        BottomNavigationRoute("reports", Icons.Outlined.Receipt, Icons.Filled.Receipt, "Reports")

    object Settings : BottomNavigationRoute(
        "settings",
        Icons.Outlined.Settings,
        Icons.Filled.Settings,
        "Settings"
    )
}