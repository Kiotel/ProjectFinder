import local.secureStore.AuthStore
import local.secureStore.FormDataStore
import local.secureStore.ProfileFillStore
import org.koin.core.module.Module
import org.koin.dsl.module
import remote.apis.AuthApi
import remote.apis.BackendApi
import remote.apis.TestApi
import repositories.AuthRepository
import repositories.AuthRepositoryImpl
import repositories.NotificationsRepository
import repositories.NotificationsRepositoryImpl
import repositories.ProjectsRepository
import repositories.ProjectsRepositoryImpl
import repositories.UsersRepository
import repositories.UsersRepositoryImpl
import utils.Logger

val dataModule = module {
    single<TestApi> { TestApi() }
    single<AuthApi> { AuthApi(get()) }
    single<BackendApi> { BackendApi(get(), get()) }

    single<AuthStore> { AuthStore(get()) }
    single<FormDataStore> { FormDataStore(get()) }
    single<ProfileFillStore> {
        ProfileFillStore(get()).also { store ->
            // При создании ProfileFillStore синхронизируем персистентный флаг в domain-слой
            get<ProfileFillManager>().restore(store.isProfileFilled())
        }
    }

    single<Logger> { Logger }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }
    single<ProjectsRepository> { ProjectsRepositoryImpl(get(), get(), get()) }
    single<UsersRepository> { UsersRepositoryImpl(get(), get(), get()) }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }

    includes(platformModule)
}

internal expect val platformModule: Module