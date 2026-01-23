package features.onBoarding

import features.onBoarding.description.descriptionModule
import features.onBoarding.greeting.greetingModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onBoardingModule = module {
    viewModelOf(::OnboardingViewModel)

    includes(descriptionModule, greetingModule)
}