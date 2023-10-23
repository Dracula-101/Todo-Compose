package io.github.dracula101.todo.common.ui

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleCoroutineScope
import io.github.dracula101.todo.common.ui.utils.UiEvent
import kotlinx.coroutines.launch

@Composable
fun ShowSnackBarEvent(
    message: String,
    actionLabel: String,
    action: () -> Unit,
) {
    val snackbarHostState = remember{
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(
        key1 = snackbarHostState,
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short
            )
        }
    }
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData: SnackbarData ->
            Snackbar(
                snackbarData = snackbarData,
            )
        }
    )
}