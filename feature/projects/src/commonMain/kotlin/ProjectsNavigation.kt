import allProjects.AllProjectsScreen
import allProjects.AllProjectsViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import createProject.CreateProjectScreen
import createProject.CreateProjectViewModel
import detailedProject.DetailedProjectScreen
import detailedProject.DetailedProjectViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProjectsNavigation(
    modifier: Modifier = Modifier,
) {
    val authBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        Route.Projects.AllProjects::class, Route.Projects.AllProjects.serializer()
                    )
                    subclass(
                        Route.Projects.Create::class, Route.Projects.Create.serializer()
                    )
                    subclass(
                        Route.Projects.DetailedProject::class,
                        Route.Projects.DetailedProject.serializer()
                    )
                }
            }
        }, Route.Projects.AllProjects
    )
    val projectsViewModel: ProjectsViewModel = koinViewModel()
    NavDisplay(
        modifier = modifier, backStack = authBackStack, entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ), entryProvider = entryProvider {
            entry<Route.Projects.AllProjects> {
                val allProjectsViewModel: AllProjectsViewModel = koinViewModel()
                val pagingFlow = allProjectsViewModel.projectsFlow
                val uiState by allProjectsViewModel.uiState.collectAsStateWithLifecycle()

                AllProjectsScreen(
                    uiState = uiState,
                    handleIntent = allProjectsViewModel::handleIntent,
                    snackBarManager = allProjectsViewModel.snackBarManager,
                    pagingFlow = pagingFlow,
                    goToProject = {
                        authBackStack.add(Route.Projects.DetailedProject(it))
                    },
                    goToCreate = {
                        authBackStack.add(Route.Projects.Create)
                    }
                )
            }
            entry<Route.Projects.Create> {
                val createProjectViewModel: CreateProjectViewModel = koinViewModel()
                val uiState by createProjectViewModel.uiState.collectAsStateWithLifecycle()

                CreateProjectScreen(
                    uiState = uiState,
                    handleIntent = createProjectViewModel::handleIntent,
                    snackBarManager = createProjectViewModel.snackBarManager,
                    onBack = { authBackStack.removeLastOrNull() }
                )
            }
            entry<Route.Projects.DetailedProject> {
                val detailedProjectViewModel: DetailedProjectViewModel = koinViewModel()
                val uiState by detailedProjectViewModel.uiState.collectAsStateWithLifecycle()

                val projectFromNavigation = it.project
                LaunchedEffect(projectFromNavigation.id) {
                    detailedProjectViewModel.setProject(projectFromNavigation)
                }

                DetailedProjectScreen(
                    uiState = uiState,
                    handleIntent = detailedProjectViewModel::handleIntent,
                    snackBarManager = detailedProjectViewModel.snackBarManager,
                )
            }
        }
    )
}