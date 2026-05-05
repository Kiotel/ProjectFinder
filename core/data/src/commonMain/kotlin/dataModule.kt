import local.secureStore.TokenStore
import org.koin.core.module.Module
import org.koin.dsl.module
import remote.apis.AuthApi
import remote.apis.BackendApi
import remote.apis.TestApi
import repositories.AuthRepository
import repositories.AuthRepositoryImpl
import repositories.ProjectRepositoryImpl
import repositories.ProjectsRepository
import utils.Logger

val dataModule = module {
    single<TestApi> { TestApi() }
    single<AuthApi> { AuthApi(get()) }
    single<BackendApi> { BackendApi(get(), get()) }

    single<TokenStore> { TokenStore(get()) }

    single<Logger> { Logger }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }
    single<ProjectsRepository> { ProjectRepositoryImpl(get(), get()) }

    includes(platformModule)
}

internal expect val platformModule: Module