package com.expense.tracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.expense.tracker.feature.add.AddNewTransactionScreen
import com.expense.tracker.feature.chart.ChartsScreen
import com.expense.tracker.feature.home.HomeScreen
import com.expense.tracker.feature.profile.ProfileScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("charts") {
            ChartsScreen(navController = navController)
        }
        composable("reports") {
            HomeScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("add") {
            AddNewTransactionScreen(navController = navController)
        }
    }
}