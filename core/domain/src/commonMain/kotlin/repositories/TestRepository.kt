package repositories

import kotlinx.coroutines.flow.Flow
import models.KtorText
import utils.Resource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface TestRepository {
    fun getPlatform(): String
    fun getKtorTextFlow(ttl: Duration = 5.minutes): Flow<Resource<KtorText>>
}