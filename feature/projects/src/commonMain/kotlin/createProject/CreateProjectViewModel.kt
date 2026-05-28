package createProject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import createProject.models.CreateProjectState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import useCases.CreateProjectUseCase
import utils.SnackBarManager

internal class CreateProjectViewModel(
    private val createProjectUseCase: CreateProjectUseCase,
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
                it.copy(roles = it.roles + (intent.name to intent.spots)) 
            }
            is CreateProjectIntent.RemoveRole -> _uiState.update {
                it.copy(roles = it.roles.filterIndexed { index, _ -> index != intent.index })
            }
            CreateProjectIntent.Submit -> submitProject()
            CreateProjectIntent.ResetSuccess -> _uiState.update { it.copy(isSuccess = false) }
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
            val result = createProjectUseCase(
                title = state.title,
                description = state.description,
                industry = state.industry,
                roles = state.roles
            )
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                snackBarManager.showMessage("Проект успешно создан!")
            }
            result.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
                snackBarManager.showMessage("Ошибка при создании проекта: ${error.message}")
            }
        }
    }
}
