package detailedProject

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val detailedProjectModule = module {
    viewModelOf(::DetailedProjectViewModel)
}
