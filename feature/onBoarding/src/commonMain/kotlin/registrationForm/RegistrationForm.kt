package registrationForm

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val registrationFormModule = module {
    viewModelOf(::RegistrationFormViewModel)
}
