import allProjects.allProjectsModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val projectsModule = module {
    viewModelOf(::ProjectsViewModel)

    includes(allProjectsModule)
}