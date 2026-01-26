package repositories

interface TestRepository {
    fun getPlatform(): String
    suspend fun getKtorText(): String
}