import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
internal fun DebugNavigationMenu(
    modifier: Modifier = Modifier,
    navigateTo: (route: Route) -> Unit
) {
    var isExpanded by remember { mutableStateOf((true)) }

    Box(
        modifier = modifier
    ) {
        Button(
            onClick = { isExpanded = true },
            content = { Text("DebugNavigation") }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(Route.Profile.toString()) },
                onClick = { navigateTo(Route.Profile) }
            )
            DropdownMenuItem(
                text = { Text(Route.OnBoarding.toString()) },
                onClick = { navigateTo(Route.OnBoarding) }
            )
        }
    }
}