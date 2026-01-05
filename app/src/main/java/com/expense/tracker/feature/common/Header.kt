package com.expense.tracker.feature.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.expense.tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(config: HeaderConfig, containerColor: Color = MaterialTheme.colorScheme.primaryContainer) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = config.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (config.navigationIcon != null)
            IconButton(onClick = config.onNavigationClick){
                Icon(
                    imageVector = config.navigationIcon,
                    contentDescription = "navigation_icon"
                )
            }
        },
        actions = {
            config.actions()
        },
        modifier = config.modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
    )
}

class HeaderConfig(
    val modifier: Modifier = Modifier,
    val title: String,
    val navigationIcon: ImageVector?,
    val onNavigationClick: () -> Unit = {},
    val actions: @Composable () -> Unit = {}
)

@Composable
fun rememberHeaderConfig(headerConfig: HeaderConfig) = remember {
    mutableStateOf(headerConfig)
}