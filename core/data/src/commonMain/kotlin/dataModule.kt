import org.koin.core.module.Module
import org.koin.dsl.module
import repositories.PlatformRepository
import repositories.PlatformRepositoryImpl

val dataModule = module {
    single<Platform> { getPlatform() }

    single<PlatformRepository> { PlatformRepositoryImpl(get()) }

    includes(platformModule)
}

expect val platformModule: Module