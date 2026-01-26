package repositories

import Platform
import remote.apis.TestApi
import local.database.TestDataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mapppers.domain
import mapppers.entity
import models.KtorText
import utils.Resource
import kotlin.time.Clock
import kotlin.time.Clock.System.now
import kotlin.time.Duration.Companion.minutes

class TestRepositoryImpl(
    private val testApi: TestApi,
    private val testDataBase: TestDataBase,
    private val platform: Platform
) : TestRepository {
    override fun getPlatform() = platform.name
    override fun getKtorTextFlow(): Flow<Resource<KtorText>> = flow {
        emit(Resource.Loading())

        val localEntity = testDataBase.KtorTextDao().getText()
        emit(Resource.Loading(data = localEntity?.domain))

        val isCacheValid =
            localEntity != null && (now().toEpochMilliseconds() - localEntity.lastUpdated < 5.minutes.inWholeMilliseconds)

        if (isCacheValid) {
            emit(Resource.Success(localEntity.domain))
            return@flow
        }

        try {
            val apiResult = testApi.test()
            testDataBase.KtorTextDao().insert(apiResult.entity)
            emit(Resource.Success(apiResult.domain))
        } catch (e: Exception) {
            println("ERROR: $e")
            emit(
                Resource.Error(
                    "Couldn't update data", localEntity?.domain
                )
            )
        }
    }
}