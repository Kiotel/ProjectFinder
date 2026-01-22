import features.onBoarding.greetingModule
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel


val uiModule = module {
    includes(greetingModule)
}

