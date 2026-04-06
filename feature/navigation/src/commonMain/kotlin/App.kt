import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import theme.AppTheme

@Composable
@Preview
fun App() {
    val navigationViewModel: NavigationViewModel = koinViewModel()
    val uiState by navigationViewModel.uiState.collectAsStateWithLifecycle()

    AppTheme {
        NavigationRoot(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            handleIntent = navigationViewModel::handleIntent
        )
    }
}

