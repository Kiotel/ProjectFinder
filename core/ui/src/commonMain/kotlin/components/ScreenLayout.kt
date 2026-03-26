package components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.getString
import utils.SnackBarManager

@Composable
fun ScreenLayout(
    modifier: Modifier = Modifier,
    snackBarManager: SnackBarManager? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    if (snackBarManager != null) {
        val currentMessage by snackBarManager.currentMessage.collectAsStateWithLifecycle()
        LaunchedEffect(currentMessage) {
            currentMessage?.let { messageState ->
                if (currentMessage?.messageResource != null) {
                    snackBarHostState.showSnackbar(
                        message = getString(
                            currentMessage!!.messageResource
                        ),
                        duration = messageState.duration
                    )
                    snackBarManager.messageShown(messageState.id)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(), containerColor = Color.Transparent, snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) { innerPadding ->
        content(innerPadding)
    }
}