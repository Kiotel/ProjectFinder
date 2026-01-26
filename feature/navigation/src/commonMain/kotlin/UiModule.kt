import features.onBoarding.onBoardingModule
import org.koin.dsl.module


val uiModule = module {
    includes(onBoardingModule)
}

