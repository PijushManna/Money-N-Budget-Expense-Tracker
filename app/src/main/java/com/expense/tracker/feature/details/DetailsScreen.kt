package com.expense.tracker.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun DetailsScreen(viewModel: DetailsViewModel = hiltViewModel(), navController: NavController) {
    val state by viewModel.state.collectAsState()
    DetailsScreenContainer(state = state, onBack = {
        navController.popBackStack()
    }, onEdit = {
        navController.navigate("add?id=$it")
    }, onDelete = {
        viewModel.deleteTransaction()
    })
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    DetailsScreenContainer(state = DetailsState(), onBack = {}, onEdit = {}, onDelete = {} )
}

