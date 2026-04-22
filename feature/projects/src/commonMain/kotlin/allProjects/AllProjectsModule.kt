package allProjects

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val allProjectsModule = module {
    viewModelOf(::AllProjectsViewModel)
}
