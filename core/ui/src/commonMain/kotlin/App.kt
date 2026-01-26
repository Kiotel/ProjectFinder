import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import navigation.NavigationRoot

@Composable
@Preview
fun App() {
    val colorScheme = if (isSystemInDarkTheme()) {
        darkColorScheme()
    } else {
        expressiveLightColorScheme()
    }
    MaterialExpressiveTheme(colorScheme = colorScheme) {
        NavigationRoot(
            modifier = Modifier.fillMaxSize()
        )
    }
}