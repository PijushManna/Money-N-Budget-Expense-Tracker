package com.expense.tracker.feature.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TripleActionDialog(
    visible: Boolean,
    title: String,
    message: String,

    confirmText: String,
    onConfirm: () -> Unit,

    rejectText: String,
    onReject: () -> Unit,

    neutralText: String? = null,
    onNeutral: (() -> Unit)? = null,

    confirmColor: Color = MaterialTheme.colorScheme.primary,
    rejectColor: Color = MaterialTheme.colorScheme.error,

    onDismiss: () -> Unit
) {
    AnimatedVisibility(visible) {
        AlertDialog(
            onDismissRequest = onDismiss,

            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },

            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },

            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    neutralText?.let {
                        TextButton(
                            onClick = {
                                onNeutral?.invoke()
                                onDismiss()
                            }
                        ) {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    TextButton(
                        onClick = {
                            onReject()
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = rejectText,
                            color = rejectColor
                        )
                    }

                    TextButton(
                        onClick = {
                            onConfirm()
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = confirmText,
                            color = confirmColor
                        )
                    }
                }
            },

            dismissButton = {},

            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
