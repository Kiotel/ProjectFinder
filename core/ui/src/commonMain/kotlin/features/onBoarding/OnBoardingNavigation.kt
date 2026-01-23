package features.onBoarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import features.onBoarding.description.DescriptionScreen
import features.onBoarding.description.DescriptionViewModel
import features.onBoarding.greeting.GreetingScreen
import features.onBoarding.greeting.GreetingViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import navigation.Route
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
                        Route.OnBoarding.Description::class,
                        Route.OnBoarding.Description.serializer()
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
                    goToDescription = { onBoardingBackStack.add(Route.OnBoarding.Description) },
                )
            }
            entry<Route.OnBoarding.Description> {
                val descriptionViewModel: DescriptionViewModel = koinViewModel()
                descriptionViewModel.setData("Text from navigation")

                DescriptionScreen(
                    vm = descriptionViewModel,
                    svm = onBoardingViewModel
                )
            }
        }

    )
}