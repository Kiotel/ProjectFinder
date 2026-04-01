import description.descriptionModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onBoardingModule = module {
    viewModelOf(::OnboardingViewModel)

    includes(descriptionModule)
}