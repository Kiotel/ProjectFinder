import allProjects.allProjectsModule
import detailedProject.detailedProjectModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val projectsModule = module {
    viewModelOf(::ProjectsViewModel)

    includes(allProjectsModule, detailedProjectModule)
}