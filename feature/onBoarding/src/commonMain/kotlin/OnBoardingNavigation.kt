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
import description.DescriptionScreen
import description.DescriptionViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnBoardingNavigation(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {},
) {
    val onBoardingBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        Route.OnBoarding.Greeting::class, Route.OnBoarding.Greeting.serializer()
                    )
                    subclass(
                        Route.OnBoarding.Description::class,
                        Route.OnBoarding.Description.serializer()
                    )
                }
            }
        }, Route.OnBoarding.Description
    )
    val onBoardingViewModel: OnboardingViewModel = koinViewModel()
    NavDisplay(
        modifier = modifier, backStack = onBoardingBackStack, entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ), entryProvider = entryProvider {
            entry<Route.OnBoarding.Description> {
                val descriptionViewModel: DescriptionViewModel = koinViewModel()
                val uiState by descriptionViewModel.uiState.collectAsStateWithLifecycle()

                // Auto-skip: если профиль уже заполнен на сервере, пропускаем форму
                LaunchedEffect(Unit) {
                    descriptionViewModel.autoNavigateEvent.collect {
                        onFinished()
                    }
                }

                DescriptionScreen(
                    uiState = uiState,
                    handleIntent = descriptionViewModel::handleIntent,
                    snackBarManager = descriptionViewModel.snackBarManager,
                    onSubmit = { descriptionViewModel.submit(onFinished) },
                )
            }
        }

    )
}