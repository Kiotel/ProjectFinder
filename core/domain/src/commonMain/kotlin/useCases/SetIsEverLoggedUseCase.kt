package useCases

import repositories.PreferencesRepository

class SetIsEverLoggedUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isEverLogged: Boolean) =
        preferencesRepository.setIsEverLogged(isEverLogged)
}