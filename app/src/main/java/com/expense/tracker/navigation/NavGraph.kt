package com.expense.tracker.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.expense.tracker.feature.add.AddNewTransactionScreen
import com.expense.tracker.feature.chart.ChartsScreen
import com.expense.tracker.feature.details.DetailsScreen
import com.expense.tracker.feature.home.HomeScreen
import com.expense.tracker.feature.profile.ProfileScreen
import slideFromBottomComposable

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController,
        "home",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
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
        slideFromBottomComposable(
            "add?id={id}", arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
                defaultValue = -1L
            })) {
            AddNewTransactionScreen(navController = navController)
        }
        composable(
            "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            DetailsScreen(navController = navController)
        }
    }
}