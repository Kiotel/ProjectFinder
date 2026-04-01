package description

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val descriptionModule = module {
    viewModelOf(::DescriptionViewModel)
}
