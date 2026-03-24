package detailedProfile

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val detailedProfileModule = module {
    viewModelOf(::DetailedProfileViewModel)
}
