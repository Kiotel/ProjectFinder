import org.koin.dsl.module
import useCases.RegisterUseCase

val domainModule = module {
    factory<RegisterUseCase> { RegisterUseCase(get()) }
}