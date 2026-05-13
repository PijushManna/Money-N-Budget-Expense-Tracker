package com.expense.tracker.utils.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.expense.tracker.utils.openAppSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.pm.expensetracker.permission.PermissionRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestSmsPermission(
    onGranted: () -> Unit
) {
    val permissionState = rememberPermissionState(Manifest.permission.READ_SMS)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if(ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED){
            onGranted()
        }else {
            permissionState.launchPermissionRequest()
        }
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
            Column {
                Text("SMS Permission permanently denied. Please enable it from settings.")

                Button(onClick = {
                    permissionState.launchPermissionRequest()
                }) {
                    Text("Request Again")
                }

                Button(onClick = {
                    openAppSettings(context)
                }) {
                    Text("Open Settings")
                }
            }
        }
    }
}