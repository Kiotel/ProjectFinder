import descriptionForm.descriptionFormModule
import greeting.greetingModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onBoardingModule = module {
    viewModelOf(::OnboardingViewModel)

    includes(descriptionFormModule, greetingModule)
}