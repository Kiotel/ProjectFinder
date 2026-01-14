package useCases

import repositories.PlatformRepository

class GetPlatformUseCase(
    private val platformRepository: PlatformRepository
) {
    operator fun invoke(): String = platformRepository.getPlatform()
}