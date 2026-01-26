package useCases

import models.KtorText
import repositories.TestRepository

class GetKtorTextUseCase(
    private val testRepository: TestRepository
) {
    suspend operator fun invoke(): Result<KtorText> = try {
        Result.success(testRepository.getKtorText())
    } catch (e: Exception) {
        Result.failure(e)
    }
}