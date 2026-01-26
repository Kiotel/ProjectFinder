package useCases

import kotlinx.coroutines.flow.Flow
import repositories.PreferencesRepository

class GetIsFilledDescriptionFormUseCase(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = preferencesRepository.getFilledDescription()
}