package com.expense.tracker.utils.permission

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.pm.expensetracker.permission.PermissionRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestSmsPermission(
    onGranted: () -> Unit,
    onDenied:() -> Unit
) {
    val permissionState = rememberPermissionState(Manifest.permission.READ_SMS)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    when {
        permissionState.status.isGranted -> {
            onGranted()
        }
        permissionState.status.shouldShowRationale -> {
            PermissionRationale {
                permissionState.launchPermissionRequest()
            }
        }
        else -> {
            onDenied()
        }
    }
}