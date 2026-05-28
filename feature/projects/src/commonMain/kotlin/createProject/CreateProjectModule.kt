package createProject

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val createProjectModule = module {
    viewModelOf(::CreateProjectViewModel)
}
