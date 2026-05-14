package components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defaults.AppNavigationBarDefaults
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
                if (currentMessage?.message != null) {
                    snackBarHostState.showSnackbar(
                        message = currentMessage!!.message,
                        duration = messageState.duration
                    )
                    snackBarManager.messageShown(messageState.id)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(), containerColor = Color.Transparent, snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = AppNavigationBarDefaults.height),
                hostState = snackBarHostState
            )
        }) { innerPadding ->
        val resultContentPadding by remember {
            derivedStateOf {
                PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = innerPadding.calculateBottomPadding() + AppNavigationBarDefaults.height
                )
            }
        }
        content(resultContentPadding)
    }
}