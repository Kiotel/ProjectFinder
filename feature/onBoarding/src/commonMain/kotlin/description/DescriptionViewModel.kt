package description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import description.models.DescriptionState
import description.models.InternalDescriptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import useCases.RegisterUseCase
import utils.SnackBarManager

internal class DescriptionViewModel(
    private val registerUseCase: RegisterUseCase,
    val snackBarManager: SnackBarManager
) : ViewModel() {
    private val _internalState = MutableStateFlow(
        InternalDescriptionState()
    )

    val uiState: StateFlow<DescriptionState> = _internalState.map { internalState ->
        DescriptionState(
            region = internalState.region,
            university = internalState.university,
            department = internalState.department,
            programme = internalState.programme,
            studyType = internalState.studyType,
            about = internalState.about,
            qualities = internalState.qualities,
            skills = internalState.skills,
            workingHours = internalState.workingHours,
            wishes = internalState.wishes,
            waysToContact = internalState.waysToContact
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DescriptionState(InternalDescriptionState())
    )


    private fun updateState(mutation: (InternalDescriptionState) -> InternalDescriptionState) {
        _internalState.update(mutation)
    }


    fun handleIntent(intent: DescriptionIntent) {
        when (intent) {
            DescriptionIntent.OnRegister -> {}
            is DescriptionIntent.SetAbout -> {
                updateState { it.copy(about = intent.newValue) }
            }

            is DescriptionIntent.SetDepartment -> {
                updateState { it.copy(department = intent.newValue) }
            }

            is DescriptionIntent.SetProgramme -> {
                updateState { it.copy(programme = intent.newValue) }
            }

            is DescriptionIntent.SetQualities -> {
                updateState { it.copy(qualities = intent.newValue) }
            }

            is DescriptionIntent.SetRegion -> {
                updateState { it.copy(region = intent.newValue) }
            }

            is DescriptionIntent.SetSkills -> {
                updateState { it.copy(skills = intent.newValue) }
            }

            is DescriptionIntent.SetStudyType -> {
                updateState { it.copy(studyType = intent.newValue) }
            }

            is DescriptionIntent.SetUniversity -> {
                updateState { it.copy(university = intent.newValue) }
            }

            is DescriptionIntent.SetWaysToContact -> {
                updateState { it.copy(waysToContact = intent.newValue) }
            }

            is DescriptionIntent.SetWishes -> {
                updateState { it.copy(wishes = intent.newValue) }
            }

            is DescriptionIntent.SetWorkingHours -> {
                updateState { it.copy(workingHours = intent.newValue) }
            }
        }
    }
}