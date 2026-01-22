package features.onBoarding

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val greetingModule = module {
    viewModelOf(::GreetingViewModel)
}
