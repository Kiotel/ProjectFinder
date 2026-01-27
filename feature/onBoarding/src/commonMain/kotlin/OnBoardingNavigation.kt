import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import descriptionForm.DescriptionFormScreen
import descriptionForm.DescriptionFormViewModel
import greeting.GreetingScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnBoardingNavigation(
    modifier: Modifier = Modifier
) {
    val onBoardingBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        Route.OnBoarding.Greeting::class,
                        Route.OnBoarding.Greeting.serializer()
                    )
                    subclass(
                        Route.OnBoarding.DescriptionForm::class,
                        Route.OnBoarding.DescriptionForm.serializer()
                    )
                }
            }
        },
        Route.OnBoarding.Greeting
    )
    val onBoardingViewModel: OnboardingViewModel = koinViewModel()
    NavDisplay(
        modifier = modifier,
        backStack = onBoardingBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.OnBoarding.Greeting> {
                val greetingViewModel: GreetingViewModel = koinViewModel()
                GreetingScreen(
                    vm = greetingViewModel,
                    svm = onBoardingViewModel,
                    goToDescriptionForm = { onBoardingBackStack.add(Route.OnBoarding.DescriptionForm) },
                )
            }
            entry<Route.OnBoarding.DescriptionForm> {
                val descriptionFormViewModel: DescriptionFormViewModel = koinViewModel()

                DescriptionFormScreen(
                    vm = descriptionFormViewModel,
                    svm = onBoardingViewModel
                )
            }
        }

    )
}