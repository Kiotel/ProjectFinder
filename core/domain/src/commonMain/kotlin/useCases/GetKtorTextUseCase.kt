package useCases

import repositories.TestRepository

class GetKtorTextUseCase(
    private val testRepository: TestRepository
) {
    suspend operator fun invoke(): Result<String> = try {
        Result.success(testRepository.getKtorText())
    } catch (e: Exception) {
        Result.failure(e)
    }
}