import org.koin.dsl.module
import useCases.GetIsAuthedUseCase
import useCases.GetProjectsUseCase
import useCases.GetUserInfoUseCase
import useCases.LikeProjectUseCase
import useCases.LoginUseCase
import useCases.RegisterUseCase

val domainModule = module {
    factory<GetIsAuthedUseCase> { GetIsAuthedUseCase(get()) }
    factory<RegisterUseCase> { RegisterUseCase(get()) }
    factory<LoginUseCase> { LoginUseCase(get()) }
    factory<GetUserInfoUseCase> { GetUserInfoUseCase(get()) }
    factory<LikeProjectUseCase> { LikeProjectUseCase(get()) }
    factory<GetProjectsUseCase> { GetProjectsUseCase(get()) }
}