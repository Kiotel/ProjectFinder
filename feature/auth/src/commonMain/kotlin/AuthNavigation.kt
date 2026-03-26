import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import login.LoginScreen
import login.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel
import registration.RegistrationScreen
import registration.RegistrationViewModel

@Composable
fun AuthNavigation(
    modifier: Modifier = Modifier,
    onAuth: () -> Unit
) {
    val authBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        Route.Auth.Registration::class, Route.Auth.Registration.serializer()
                    )
                    subclass(
                        Route.Auth.Login::class,
                        Route.Auth.Login.serializer()
                    )
                }
            }
        }, Route.Auth.Login
    )
    val authViewModel: AuthViewModel = koinViewModel()
    NavDisplay(
        modifier = modifier, backStack = authBackStack, entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ), entryProvider = entryProvider {
            entry<Route.Auth.Registration> {
                val registrationViewModel: RegistrationViewModel = koinViewModel()
                val uiState by registrationViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(uiState.isAuthed) {
                    if (uiState.isAuthed) onAuth()
                }

                RegistrationScreen(
                    uiState = uiState,
                    handleIntent = registrationViewModel::handleIntent,
                    snackBarManager = registrationViewModel.snackBarManager
                )
            }
            entry<Route.Auth.Login> {
                val loginViewModel: LoginViewModel = koinViewModel()
                val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(uiState.isAuthed) {
                    if (uiState.isAuthed) onAuth()
                }

                LoginScreen(
                    uiState = uiState,
                    handleIntent = loginViewModel::handleIntent,
                    snackBarManager = loginViewModel.snackBarManager,
                    goToRegister = { authBackStack.add(Route.Auth.Registration) },
                )
            }
        }

    )
}