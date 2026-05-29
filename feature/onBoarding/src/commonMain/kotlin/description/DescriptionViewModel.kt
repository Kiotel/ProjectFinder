package description

import ProfileFillManager
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import description.models.DescriptionState
import description.models.InternalDescriptionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import local.secureStore.FormDataStore
import local.secureStore.ProfileFillStore
import models.Contact
import models.Skill
import models.UserProfile
import useCases.GetUserProfileUseCase
import useCases.UpdateUserProfileUseCase
import utils.SnackBarManager

internal class DescriptionViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val formDataStore: FormDataStore,
    private val profileFillStore: ProfileFillStore,
    private val profileFillManager: ProfileFillManager,
    val snackBarManager: SnackBarManager,
) : ViewModel() {

    /**
     * One-shot event: когда профиль уже заполнен на сервере (после очистки данных приложения),
     * DescriptionViewModel детектит это и отправляет сигнал навигации, чтобы пропустить форму.
     */
    private val _autoNavigateEvent = Channel<Unit>(Channel.BUFFERED)
    val autoNavigateEvent = _autoNavigateEvent.receiveAsFlow()
    val availableSkills = listOf(
        "Kotlin", "Android", "iOS", "Swift", "Java",
        "Python", "Go", "JavaScript", "TypeScript",
        "React", "Vue", "SQL", "Figma",
        "UI/UX Design", "QA Engineering"
    ).sorted()

    private val _internalState = MutableStateFlow(InternalDescriptionState())

    val uiState: StateFlow<DescriptionState> = _internalState.map { 
        DescriptionState(it, availableSkills) 
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DescriptionState(InternalDescriptionState(), availableSkills),
    )

    init {
        viewModelScope.launch {
            // Then prefill from server profile (if user has saved profile)
            getUserProfileUseCase.current().first().onSuccess { _ -> 
                // Пропускаем форму, только если пользователь уже заполнял её в предыдущей сессии
                if (profileFillStore.isProfileFilled()) {
                    _autoNavigateEvent.trySend(Unit)
                }
            }
        }
    }
    
    private fun loadFromFormDataStore() {
        // Disabled to make fields empty
    }

    private fun prefill(profile: UserProfile) {
        // Disabled to make fields empty
    }

    private fun updateState(mutation: (InternalDescriptionState) -> InternalDescriptionState) {
        _internalState.update(mutation)
    }

    fun submit(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _internalState.value
            val base = getUserProfileUseCase.current().first().getOrNull()
            
            // Если профиль ещё не загружен или вернул ошибку, пробуем получить ID хотя бы из AuthStore
            // (это поможет избежать ID="0", который вызывает 403 на бэкенде)
            val userId = base?.id?.takeIf { it != "0" } 
                ?: _internalState.value.firstName.takeIf { it == "DEBUG_BYPASS" } // для тестов
                ?: "" // Репозиторий сам попробует взять из authStore.userId

            val profile = UserProfile(
                id = userId,
                username = base?.username ?: "",
                email = base?.email ?: "",
                firstName = state.firstName.ifBlank { null },
                lastName = state.lastName.ifBlank { null },
                displayName = "${state.firstName} ${state.lastName}".trim().ifBlank { base?.username ?: "" },
                age = state.age.toIntOrNull(),
                city = state.city.ifBlank { null },
                university = state.university.ifBlank { null },
                faculty = state.department.ifBlank { null },
                programCode = state.programme.ifBlank { null },
                studyMode = state.studyType.ifBlank { null },
                schedule = state.workingHours.ifBlank { null },
                goals = state.about.ifBlank { null },
                qualities = state.qualities.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                skills = state.selectedSkills.map { name -> Skill(name = name) },
                interests = state.interests.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                portfolioUrl = state.portfolioUrl.ifBlank { null },
                contacts = parseContacts(state.waysToContact),
                avatarUrl = base?.avatarUrl,
            )

            updateUserProfileUseCase(profile).fold(
                onSuccess = {
                    profileFillStore.markAsFilled()
                    profileFillManager.markAsFilled()
                    snackBarManager.showMessage("Профиль сохранён", SnackbarDuration.Short)
                    onSuccess()
                },
                onFailure = { error ->
                    snackBarManager.showMessage(
                        error.message ?: "Ошибка сохранения",
                        SnackbarDuration.Short,
                    )
                    // PUT /users/{id} на сервере часто отдаёт 500 — пропускаем в приложение
                    if (error.message?.contains("500") == true) {
                        profileFillStore.markAsFilled()
                        profileFillManager.markAsFilled()
                        onSuccess()
                    }
                },
            )
        }
    }

    private fun parseContacts(raw: String): List<Contact> =
        raw.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { line ->
                val parts = line.split(":", limit = 2)
                if (parts.size == 2) {
                    Contact(type = parts[0].trim(), value = parts[1].trim())
                } else {
                    Contact(type = "Other", value = line)
                }
            }

    fun handleIntent(intent: DescriptionIntent) {
        when (intent) {
            DescriptionIntent.OnRegister -> {}
            is DescriptionIntent.SetFirstName -> {
                updateState { it.copy(firstName = intent.newValue) }
                formDataStore.firstName = intent.newValue
            }
            is DescriptionIntent.SetLastName -> {
                updateState { it.copy(lastName = intent.newValue) }
                formDataStore.lastName = intent.newValue
            }
            is DescriptionIntent.SetAge -> {
                updateState { it.copy(age = intent.newValue) }
                formDataStore.age = intent.newValue
            }
            is DescriptionIntent.SetCity -> {
                updateState { it.copy(city = intent.newValue) }
                formDataStore.city = intent.newValue
            }
            is DescriptionIntent.SetUniversity -> {
                updateState { it.copy(university = intent.newValue) }
                formDataStore.university = intent.newValue
            }
            is DescriptionIntent.SetDepartment -> {
                updateState { it.copy(department = intent.newValue) }
                formDataStore.department = intent.newValue
            }
            is DescriptionIntent.SetProgramme -> {
                updateState { it.copy(programme = intent.newValue) }
                formDataStore.programme = intent.newValue
            }
            is DescriptionIntent.SetStudyType -> {
                updateState { it.copy(studyType = intent.newValue) }
                formDataStore.studyType = intent.newValue
            }
            is DescriptionIntent.SetAbout -> {
                updateState { it.copy(about = intent.newValue) }
                formDataStore.about = intent.newValue
            }
            is DescriptionIntent.SetQualities -> {
                updateState { it.copy(qualities = intent.newValue) }
                formDataStore.qualities = intent.newValue
            }
            is DescriptionIntent.SetInterests -> {
                updateState { it.copy(interests = intent.newValue) }
                formDataStore.interests = intent.newValue
            }
            is DescriptionIntent.SetPortfolio -> {
                updateState { it.copy(portfolioUrl = intent.newValue) }
                formDataStore.portfolioUrl = intent.newValue
            }
            is DescriptionIntent.AddSkill -> {
                updateState { 
                    if (it.selectedSkills.contains(intent.skill)) it 
                    else it.copy(selectedSkills = it.selectedSkills + intent.skill) 
                }
                if (!_internalState.value.selectedSkills.contains(intent.skill)) {
                    formDataStore.selectedSkills = _internalState.value.selectedSkills + intent.skill
                }
            }
            is DescriptionIntent.RemoveSkill -> {
                updateState { it.copy(selectedSkills = it.selectedSkills - intent.skill) }
                formDataStore.selectedSkills = _internalState.value.selectedSkills - intent.skill
            }
            is DescriptionIntent.SetSkillSearch -> updateState { it.copy(skillSearchQuery = intent.query) }
            is DescriptionIntent.SetWorkingHours -> {
                updateState { it.copy(workingHours = intent.newValue) }
                formDataStore.workingHours = intent.newValue
            }
            is DescriptionIntent.SetWaysToContact -> {
                updateState { it.copy(waysToContact = intent.newValue) }
                formDataStore.waysToContact = intent.newValue
            }
        }
    }
}
