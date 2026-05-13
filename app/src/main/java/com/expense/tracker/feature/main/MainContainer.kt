package com.expense.tracker.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.expense.tracker.navigation.NavGraph
import com.expense.tracker.utils.permission.RequestSmsPermission

@Composable
fun MainContainer() {
    var isSmsPermissionGranted by remember { mutableStateOf(false) }
    RequestSmsPermission{
        isSmsPermissionGranted = true
    }
    if (isSmsPermissionGranted) {
        NavGraph()
    }
}