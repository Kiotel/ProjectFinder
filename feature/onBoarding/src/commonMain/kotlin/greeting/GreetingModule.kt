package greeting

import GreetingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val greetingModule = module {
    viewModelOf(::GreetingViewModel)
}
