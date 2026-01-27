import org.koin.core.module.Module
import org.koin.dsl.module
import remote.apis.TestApi
import repositories.AuthRepository
import repositories.AuthRepositoryImpl

val dataModule = module {
    single<TestApi> { TestApi() }

    single<AuthRepository> { AuthRepositoryImpl(get()) }

    includes(platformModule)
}

expect val platformModule: Module