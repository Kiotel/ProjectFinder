import login.loginModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import registration.registrationModule

val authModule = module {
    viewModelOf(::AuthViewModel)

    includes(registrationModule, loginModule)
}