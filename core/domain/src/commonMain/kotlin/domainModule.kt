import org.koin.dsl.module
import useCases.GetPlatformUseCase
import kotlin.coroutines.EmptyCoroutineContext.get

val domainModule = module {
    factory<GetPlatformUseCase> { GetPlatformUseCase(get()) }
}