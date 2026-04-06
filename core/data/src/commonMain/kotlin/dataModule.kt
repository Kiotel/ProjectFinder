import local.secureStore.TokenStore
import org.koin.core.module.Module
import org.koin.dsl.module
import remote.apis.AuthApi
import remote.apis.BackendApi
import remote.apis.TestApi
import repositories.AuthRepository
import repositories.AuthRepositoryImpl
import utils.Logger

val dataModule = module {
    single<TestApi> { TestApi() }
    single<AuthApi> { AuthApi(get()) }
    single<BackendApi> { BackendApi(get()) }

    single<TokenStore> { TokenStore(get()) }

    single<Logger> { Logger }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }

    includes(platformModule)
}

expect val platformModule: Module