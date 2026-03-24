import detailedProfile.detailedProfileModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    viewModelOf(::ProfileViewModel)

    includes(detailedProfileModule)
}