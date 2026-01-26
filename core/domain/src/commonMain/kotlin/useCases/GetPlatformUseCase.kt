package useCases

import repositories.TestRepository

class GetPlatformUseCase(
    private val testRepository: TestRepository
) {
    operator fun invoke(): String = testRepository.getPlatform()
}