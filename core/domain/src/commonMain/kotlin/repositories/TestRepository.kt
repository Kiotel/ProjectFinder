package repositories

import kotlinx.coroutines.flow.Flow
import models.KtorText
import utils.Resource

interface TestRepository {
    fun getPlatform(): String
    fun getKtorTextFlow(): Flow<Resource<KtorText>>
}