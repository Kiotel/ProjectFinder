package useCases

import kotlinx.coroutines.flow.Flow
import models.KtorText
import repositories.TestRepository
import utils.Resource

class GetKtorTextUseCase(
    private val testRepository: TestRepository
) {
    operator fun invoke(): Flow<Resource<KtorText>> = testRepository.getKtorTextFlow()
}