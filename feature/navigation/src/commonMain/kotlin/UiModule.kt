import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import utils.SnackBarManager


val uiModule = module {
    viewModelOf(::NavigationViewModel)

    singleOf(::SnackBarManager)

    includes(onBoardingModule, profileModule, authModule, projectsModule)
}

