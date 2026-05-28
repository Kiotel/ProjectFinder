import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
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
    currentRoute: Route,
) {
    NavigationBar(
        modifier = modifier.height(AppNavigationBarDefaults.height).cheapGlassEffect(),
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = Transparent,
    ) {
        NavigationBarItem(
            selected = currentRoute is Route.Projects,
            onClick = { navigateTo(Route.Projects) },
            icon = { Icon(Icons.Default.ViewList, contentDescription = null) },
            label = { Text("Лента") },
        )
        NavigationBarItem(
            selected = currentRoute is Route.Search,
            onClick = { navigateTo(Route.Search) },
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("Поиск") },
        )
        NavigationBarItem(
            selected = currentRoute is Route.Notifications,
            onClick = { navigateTo(Route.Notifications) },
            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
            label = { Text("Уведомления") },
        )
        NavigationBarItem(
            selected = currentRoute is Route.Profile,
            onClick = { navigateTo(Route.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Профиль") },
        )
    }
}
