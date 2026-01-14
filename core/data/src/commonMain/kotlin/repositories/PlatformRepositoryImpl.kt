package repositories

import Platform
import getPlatform as getPlatformData

class PlatformRepositoryImpl(
    private val platform: Platform
) : PlatformRepository {
    override fun getPlatform() = platform.name
}