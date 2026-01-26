import org.koin.core.module.Module
import org.koin.dsl.module
import remote.apis.TestApi
import repositories.PreferencesRepository
import repositories.PreferencesRepositoryImpl
import repositories.TestRepository
import repositories.TestRepositoryImpl

val dataModule = module {
    single<Platform> { getPlatform() }

    single<TestApi> { TestApi() }

    single<TestRepository> { TestRepositoryImpl(get(), get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

    includes(platformModule)
}

expect val platformModule: Module