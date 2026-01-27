import org.koin.dsl.module
import useCases.GetTokenUseCase
import useCases.SetTokenUseCase

val domainModule = module {
    factory<GetTokenUseCase> { GetTokenUseCase(get()) }
    factory<SetTokenUseCase> { SetTokenUseCase(get()) }
}