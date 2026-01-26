package useCases

import kotlinx.coroutines.flow.Flow
import repositories.PreferencesRepository

class GetIsEverLoggedUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = preferencesRepository.getIsEverLogged()
}