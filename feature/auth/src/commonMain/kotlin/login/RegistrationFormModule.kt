package login

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val loginModule = module {
    viewModelOf(::LoginViewModel)
}
