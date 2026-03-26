package repositories

import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import local.database.UserDataBase
import local.datastore.TokenStore
import mapppers.toDomain
import mapppers.toEntity
import models.User
import remote.apis.AuthApi
import remote.apis.dtos.common.UserDto
import remote.apis.dtos.responses.ResponseLoginDto
import remote.apis.dtos.responses.ResponseRegisterDto
import utils.Logger
import utils.Logger.e
import kotlin.text.isBlank
import kotlin.time.Clock
import kotlin.time.Clock.System.now
import kotlin.time.Instant

class AuthRepositoryImpl(
    private val tokenStore: TokenStore,
    private val authApi: AuthApi,
    private val logger: Logger,
    private val userDataBase: UserDataBase,
) : AuthRepository {
    override suspend fun logOut() {
        logger.i(
            "AuthRepositoryImpl/logOut", "Started wiping all auth data"
        )
        try {
            tokenStore.setRefreshToken(null)
            tokenStore.setAccessToken(null)
            userDataBase.userDao().get()?.let { userDataBase.userDao().delete(it) }
        } catch (e: Exception) {
            logger.e(
                "AuthRepositoryImpl/logOut",
                "Couldn't wipe all auth data. Error: ${e.stackTraceToString()}"
            )
            return
        }
        logger.i(
            "AuthRepositoryImpl/logOut", "Successfully wiped all auth data"
        )
    }


    override fun register(username: String, email: String, password: String): Flow<Result<Unit>> =
        flow {
            logger.i(
                "AuthRepositoryImpl/register",
                "Started registration.\nusername: $username\nemail:$email\npassword:$password"
            )

            val response = authApi.register(
                username = username, email = email, password = password
            )

            if (response.status.value in 200..299) {
                val result = response.body<ResponseRegisterDto>()

                val accessToken = result.accessToken.takeUnless { it.isBlank() }
                    ?: error("Access token is empty in response")
                val refreshToken = result.refreshToken.takeUnless { it.isBlank() }
                    ?: error("Refresh token is empty in response")

                val userInfo = result.userDto
                userDataBase.userDao().upsert(userInfo.toEntity())

                tokenStore.setAccessToken(accessToken)
                tokenStore.setRefreshToken(refreshToken)

                logger.i(
                    "AuthRepositoryImpl/register",
                    "Successful register. Tokens: $accessToken and $refreshToken"
                )

                emit(Result.success(Unit))
            }
            if (response.status.value in 400..499) {
                logger.w(
                    "AuthRepositoryImpl/register", "Client error"
                )

                emit(Result.failure(Throwable("АШИБКА")))
            }
        }.catch { e ->
            logger.e("AuthRepositoryImpl/register", "Exception: $e")
            emit(Result.failure(e))
        }

    override fun login(
        email: String, password: String
    ): Flow<Result<Unit>> = flow {
        logger.i(
            "AuthRepositoryImpl/login", "Started login:\t\nemail:$email\t\npassword:$password"
        )

        val response = authApi.login(
            email = email, password = password
        )

        if (response.status.value in 200..299) {
            val result = response.body<ResponseLoginDto>()

            val accessToken = result.accessToken.takeUnless { it.isBlank() }
                ?: error("Access token is empty in response")
            val refreshToken = result.refreshToken.takeUnless { it.isBlank() }
                ?: error("Refresh token is empty in response")
            val userInfo = result.userDto

            userDataBase.userDao().upsert(userInfo.toEntity())
            tokenStore.setAccessToken(accessToken)
            tokenStore.setRefreshToken(refreshToken)

            logger.i(
                "AuthRepositoryImpl/login",
                "Successful login. Tokens: $accessToken and $refreshToken"
            )

            emit(Result.success(Unit))
        }
        if (response.status.value in 400..499) {
            logger.w(
                "AuthRepositoryImpl/login", "Client error"
            )

            emit(Result.failure(Throwable("АШИБКА ЮЗЕРА")))
        }
    }.catch { e ->
        logger.e("AuthRepositoryImpl/login", "Exception: $e")
        emit(Result.failure(e))
    }

    override suspend fun getUserInfo(cacheTtl: Long): Result<User> {
        logger.i(
            "AuthRepositoryImpl/getUserInfo", "Started getting userInfo"
        )
        val localEntity = userDataBase.userDao().get()

        val isCacheValid =
            localEntity != null && (now().toEpochMilliseconds() - localEntity.lastUpdated < cacheTtl)

        if (localEntity == null) {
            logger.i(
                "AuthRepositoryImpl/getUserInfo", "Local user is null"
            )
        } else {
            logger.i(
                "AuthRepositoryImpl/getUserInfo", """Found local entity:
                    |id: ${localEntity.id}
                    |userName: ${localEntity.userName}
                    |email: ${localEntity.email},
                    |fullName: ${localEntity.fullName},
                    |avatarUrl: ${localEntity.avatarUrl},
                    |lastUpdated: ${
                    Instant.fromEpochSeconds(localEntity.lastUpdated)
                }""".trimMargin()
            )
        }

        if (isCacheValid) {
            logger.i(
                "AuthRepositoryImpl/getUserInfo", "Local entity has valid ttl"
            )
            return Result.success(localEntity.toDomain())
        }
        logger.i(
            "AuthRepositoryImpl/getUserInfo", "Local entity doesn't have valid ttl"
        )

        try {
            logger.i(
                "AuthRepositoryImpl/getUserInfo", "Fetching userInfo"
            )
            // TODO: Сделать обновление пользователя по токену и доделать код ниже(он неправильный)
            // fetch user()
            val apiResult = UserDto(
                id = "123", username = "321", email = "3123", fullName = null, avatarUrl = null
            )
            userDataBase.userDao().upsert(apiResult.toEntity())
            logger.i(
                "AuthRepositoryImpl/getUserInfo", "Successfully fetched and updated userInfo"
            )
            return Result.success(apiResult.toDomain())
        } catch (e: Exception) {
            logger.e(
                "AuthRepositoryImpl/getUserInfo",
                "Error during fetching and updating of userInfo. Error: ${e.stackTraceToString()}"
            )
            return Result.failure(Throwable("Couldn't update data"))
        }
    }

}

