import local.secureStore.AuthStore
import org.koin.core.module.Module
import org.koin.dsl.module
import remote.apis.AuthApi
import remote.apis.BackendApi
import remote.apis.TestApi
import repositories.AuthRepository
import repositories.AuthRepositoryImpl
import repositories.ProjectsRepository
import repositories.ProjectsRepositoryImpl
import utils.Logger

val dataModule = module {
    single<TestApi> { TestApi() }
    single<AuthApi> { AuthApi(get()) }
    single<BackendApi> { BackendApi(get(), get()) }

    single<AuthStore> { AuthStore(get()) }

    single<Logger> { Logger }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(), get()) }

    includes(platformModule)
}

internal expect val platformModule: Module