package useCases

import repositories.PreferencesRepository

class SetIsFilledDescriptionForm(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isFilledDescriptionForm: Boolean) =
        preferencesRepository.setFilledDescription(isFilledDescriptionForm)
}