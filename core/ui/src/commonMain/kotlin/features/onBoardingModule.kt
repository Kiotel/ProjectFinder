package features

import features.onBoarding.greetingModule
import org.koin.dsl.module

val onBoardingModule = module {
    includes(greetingModule)
}