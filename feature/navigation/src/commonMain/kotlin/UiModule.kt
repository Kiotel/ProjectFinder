import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import utils.SnackBarManager


val uiModule = module {
    singleOf(::SnackBarManager)

    includes(onBoardingModule, profileModule, authModule)
}

