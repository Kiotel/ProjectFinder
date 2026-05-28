import allProjects.allProjectsModule
import createProject.createProjectModule
import detailedProject.detailedProjectModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val projectsModule = module {
    viewModelOf(::ProjectsViewModel)

    includes(allProjectsModule, detailedProjectModule, createProjectModule)
}