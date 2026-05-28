import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import detailedProfile.DetailedProfileScreen
import detailedProfile.DetailedProfileViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileNavigation(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    val profileBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Profile.Detailed::class, Route.Profile.Detailed.serializer())
                }
            }
        },
        Route.Profile.Detailed,
    )
    NavDisplay(
        modifier = modifier,
        backStack = profileBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Profile.Detailed> {
                val vm: DetailedProfileViewModel = koinViewModel()
                DetailedProfileScreen(
                    vm = vm,
                    onLogout = onLogout,
                    onDeleteAccount = onDeleteAccount,
                )
            }
        },
    )
}
