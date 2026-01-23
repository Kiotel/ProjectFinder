package features.onBoarding.description

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val descriptionModule = module {
    viewModelOf(::DescriptionViewModel)
}
