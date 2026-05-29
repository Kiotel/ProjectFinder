package createProject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import createProject.models.CreateProjectState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.Project
import useCases.CreateProjectUseCase
import useCases.UpdateProjectUseCase
import utils.SnackBarManager

internal class CreateProjectViewModel(
    private val createProjectUseCase: CreateProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    val snackBarManager: SnackBarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProjectState())
    val uiState: StateFlow<CreateProjectState> = _uiState.asStateFlow()

    fun handleIntent(intent: CreateProjectIntent) {
        when (intent) {
            is CreateProjectIntent.SetTitle -> _uiState.update { it.copy(title = intent.title) }
            is CreateProjectIntent.SetDescription -> _uiState.update { it.copy(description = intent.description) }
            is CreateProjectIntent.SetIndustry -> _uiState.update { it.copy(industry = intent.industry) }
            is CreateProjectIntent.AddRole -> _uiState.update {
                it.copy(roles = it.roles + models.ProjectRole(name = intent.name, spots = intent.spots))
            }
            is CreateProjectIntent.RemoveRole -> _uiState.update {
                it.copy(roles = it.roles.filterIndexed { index, _ -> index != intent.index })
            }
            is CreateProjectIntent.AddSkillToRole -> _uiState.update {
                it.copy(roles = it.roles.mapIndexed { index, role ->
                    if (index == intent.roleIndex) {
                        role.copy(skills = if (intent.skill in role.skills) role.skills else role.skills + intent.skill)
                    } else role
                })
            }
            is CreateProjectIntent.RemoveSkillFromRole -> _uiState.update {
                it.copy(roles = it.roles.mapIndexed { index, role ->
                    if (index == intent.roleIndex) {
                        role.copy(skills = role.skills - intent.skill)
                    } else role
                })
            }
            CreateProjectIntent.Submit -> submitProject()
            CreateProjectIntent.ResetSuccess -> _uiState.update { it.copy(isSuccess = false) }
        }
    }

    fun setProjectToEdit(project: Project) {
        _uiState.update {
            it.copy(
                projectId = project.id,
                title = project.title,
                description = project.description,
                industry = project.industry ?: "",
                roles = project.roles
            )
        }
    }

    private fun submitProject() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            viewModelScope.launch { snackBarManager.showMessage("Название проекта не может быть пустым") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = if (state.projectId != null) {
                updateProjectUseCase(
                    projectId = state.projectId,
                    title = state.title,
                    description = state.description,
                    industry = state.industry,
                    roles = state.roles
                )
            } else {
                createProjectUseCase(
                    title = state.title,
                    description = state.description,
                    industry = state.industry,
                    roles = state.roles
                )
            }
            
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                snackBarManager.showMessage(if (state.projectId != null) "Проект обновлён!" else "Проект успешно создан!")
            }
            result.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
                snackBarManager.showMessage("Ошибка: ${error.message}")
            }
        }
    }
}
