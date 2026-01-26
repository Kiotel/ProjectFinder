import org.koin.dsl.module
import useCases.GetIsEverLoggedUseCase
import useCases.GetIsFilledDescriptionFormUseCase
import useCases.GetKtorTextUseCase
import useCases.GetPlatformUseCase
import useCases.SetIsEverLoggedUseCase
import useCases.SetIsFilledDescriptionForm

val domainModule = module {
    factory<GetPlatformUseCase> { GetPlatformUseCase(get()) }
    factory<GetKtorTextUseCase> { GetKtorTextUseCase(get()) }
    factory<GetIsFilledDescriptionFormUseCase> { GetIsFilledDescriptionFormUseCase(get()) }
    factory<SetIsFilledDescriptionForm> { SetIsFilledDescriptionForm(get()) }
    factory<GetIsEverLoggedUseCase> { GetIsEverLoggedUseCase(get()) }
    factory<SetIsEverLoggedUseCase> { SetIsEverLoggedUseCase(get()) }
}