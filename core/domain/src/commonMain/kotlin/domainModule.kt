import org.koin.dsl.module
import useCases.GetPlatformUseCase
import useCases.GetKtorTextUseCase

val domainModule = module {
    factory<GetPlatformUseCase> { GetPlatformUseCase(get()) }
    factory<GetKtorTextUseCase> { GetKtorTextUseCase(get()) }
}