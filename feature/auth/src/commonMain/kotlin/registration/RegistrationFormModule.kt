package registration

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val registrationModule = module {
    viewModelOf(::RegistrationViewModel)
}
