package com.expense.tracker.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.expense.tracker.feature.recurring.ui.ManageRecurringPaymentsScreen
import com.expense.tracker.feature.recurring.ui.RecurringPaymentScreen
import slideFromBottomComposable

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController,
        "home",
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Charts.route) {
            ChartsScreen(navController = navController)
        }
        composable(Screen.Reports.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
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
        slideFromBottomComposable(Screen.RecurringPayment.route) {
            RecurringPaymentScreen() {
                navController.popBackStack()
            }
        }
        composable(Screen.ManageRecurringPayment.route) {
            ManageRecurringPaymentsScreen(navController = navController)
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Charts : Screen("charts")
    object Reports : Screen("reports")
    object Profile : Screen("profile")
    object Add : Screen("add")
    object Details : Screen("details")
    object RecurringPayment : Screen("recurring_payment")
    object ManageRecurringPayment : Screen("manage_rp")
}
