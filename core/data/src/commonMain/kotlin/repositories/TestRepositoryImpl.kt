package repositories

import Platform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import local.database.TestDataBase
import mapppers.domain
import mapppers.entity
import models.KtorText
import remote.apis.TestApi
import utils.Resource
import kotlin.time.Clock.System.now
import kotlin.time.Duration

class TestRepositoryImpl(
    private val testApi: TestApi,
    private val testDataBase: TestDataBase,
    private val platform: Platform
) : TestRepository {
    override fun getPlatform() = platform.name
    override fun getKtorTextFlow(ttl: Duration): Flow<Resource<KtorText>> = flow {
        emit(Resource.Loading())

        val localEntity = testDataBase.KtorTextDao().getText()
        emit(Resource.Loading(data = localEntity?.domain))

        val isCacheValid =
            localEntity != null && (now().toEpochMilliseconds() - localEntity.lastUpdated < ttl.inWholeMilliseconds)

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