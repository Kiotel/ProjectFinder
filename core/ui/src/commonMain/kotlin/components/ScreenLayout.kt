package components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ScreenLayout(
    modifier: Modifier = Modifier,
    snackBarId: Int = 0,
    snackBarText: String? = null,
    snackBarDuration: SnackbarDuration = SnackbarDuration.Short,
    content: @Composable (PaddingValues) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackBarId) {
        if (snackBarId != 0 && snackBarText != null) {
            snackBarHostState.showSnackbar(
                message = snackBarText,
                duration = snackBarDuration,
                withDismissAction = true
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(), containerColor = Color.Transparent, snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) { innerPadding ->
        content(innerPadding)
    }
}