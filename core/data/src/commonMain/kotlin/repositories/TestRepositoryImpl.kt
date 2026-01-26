package repositories

import Platform
import apis.TestApi
import database.TestDataBase
import models.KtorText

class TestRepositoryImpl(
    private val testApi: TestApi,
    private val testDataBase: TestDataBase,
    private val platform: Platform
) : TestRepository {
    override fun getPlatform() = platform.name
    override suspend fun getKtorText(): KtorText {
        val result = testDataBase.KtorTextDao().getText()?.toDomain() ?: testApi.test().toDomain()
        return result
    }
}