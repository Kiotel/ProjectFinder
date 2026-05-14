import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Grid3x3
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import defaults.AppNavigationBarDefaults
import modifiers.cheapGlassEffect

@Composable
internal fun AppNavigationBar(
    modifier: Modifier = Modifier,
    navigateTo: (route: Route) -> Unit,
    currentRoute: Route
) {
    NavigationBar(
        modifier = modifier.height(AppNavigationBarDefaults.height).cheapGlassEffect(),
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = Transparent
    ) {
        NavigationBarItem(
            selected = currentRoute == Route.Profile,
            onClick = {
                navigateTo(Route.Profile)
            },
            icon = {
                Icon(
                    Icons.Default.Apps,
                    contentDescription = null
                )
            },
            label = { Text("Профиль") }
        )
        NavigationBarItem(
            selected = currentRoute == Route.Projects,
            onClick = {
                navigateTo(Route.Projects)
            },
            icon = {
                Icon(
                    Icons.Default.Grid3x3,
                    contentDescription = null
                )
            },
            label = { Text("Проекты") }
        )
    }
}