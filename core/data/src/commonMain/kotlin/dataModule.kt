import apis.TestApi
import database.TestDataBase
import database.enitities.KtorTextDao
import org.koin.core.module.Module
import org.koin.dsl.module
import repositories.TestRepository
import repositories.TestRepositoryImpl

val dataModule = module {
    single<Platform> { getPlatform() }

    single<TestApi> { TestApi() }

    single<TestRepository> { TestRepositoryImpl(get(), get(), get()) }

    includes(platformModule)
}

expect val platformModule: Module