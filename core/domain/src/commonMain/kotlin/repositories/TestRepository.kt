package repositories

import models.KtorText

interface TestRepository {
    fun getPlatform(): String
    suspend fun getKtorText(): KtorText
}