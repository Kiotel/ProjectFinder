import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        NavigationRoot(
            modifier = Modifier.fillMaxSize()
        )
    }
}