package com.expense.tracker.feature.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(config: HeaderConfig) {
    TopAppBar(
        title = {
            Text(
                text = config.title
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
    )
}

class HeaderConfig(
    val modifier: Modifier = Modifier,
    val title: String,
    val navigationIcon: ImageVector? = null,
    val onNavigationClick: () -> Unit = {},
    val actions: @Composable () -> Unit = {}
)

@Composable
fun rememberHeaderConfig(headerConfig: HeaderConfig) = remember {
    mutableStateOf(headerConfig)
}