package repositories

import Platform
import apis.TestApi

class TestRepositoryImpl(
    private val testApi: TestApi,
    private val platform: Platform
) : TestRepository {
    override fun getPlatform() = platform.name
    override suspend fun getKtorText(): String = testApi.test()
}