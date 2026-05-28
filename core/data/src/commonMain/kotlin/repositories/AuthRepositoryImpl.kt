package repositories

import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import local.database.UserDataBase
import local.secureStore.AuthStore
import mapppers.toEntity
import remote.apis.AuthApi
import remote.apis.dtos.responses.ResponseLoginDto
import remote.apis.dtos.responses.ResponseRefreshTokenDto
import remote.apis.dtos.responses.ResponseRegisterDto
import utils.Logger

internal class AuthRepositoryImpl(
    private val authStore: AuthStore,
    private val authApi: AuthApi,
    private val logger: Logger,
    private val userDataBase: UserDataBase,
) : AuthRepository {
    override suspend fun logOut() {
        logger.i(
            "AuthRepositoryImpl/logOut", "Started wiping all auth data"
        )
        try {
            authStore.setAuthData("", "", "")
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

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val response = authApi.deleteAccount()
            if (response.status.value in 200..299) {
                logOut()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка удаления аккаунта: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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

                val userId = result.userDto.id?.toString()
                    ?: error("User id is empty in response")
                val accessToken = result.accessToken.takeUnless { it.isBlank() }
                    ?: error("Access token is empty in response")
                val refreshToken = result.refreshToken.takeUnless { it.isBlank() }
                    ?: error("Refresh token is empty in response")

                authStore.setAuthData(
                    userId = userId,
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )

                logger.i(
                    "AuthRepositoryImpl/register",
                    "Successful register. UserId: $userId, Tokens: $accessToken and $refreshToken"
                )

                emit(Result.success(Unit))
            } else {
                val errorMsg = "Ошибка регистрации (HTTP ${response.status.value})"
                logger.e("AuthRepositoryImpl/register", errorMsg)
                emit(Result.failure(Throwable(errorMsg)))
            }
        }.catch { e ->
            logger.e("AuthRepositoryImpl/register", "Exception: $e")
            emit(Result.failure(e))
        }

    override fun isAuthed(): Flow<Result<Unit>> =
        flow {
            logger.i(
                "AuthRepositoryImpl/isAuthed",
                "Checking is authed...."
            )

            val response = authApi.refreshAuthToken(
                authStore.refreshToken
            )

            logger.i(
                "AuthRepositoryImpl/isAuthed",
                "Server response is: $response"
            )

            if (response.status.value in 200..299) {
                val result = response.body<ResponseRefreshTokenDto>()

                val user = result.userDto ?: error("User data is missing in response")
                val userId = user.id?.toString() ?: error("User id is missing in response")
                val accessToken = result.accessToken?.takeUnless { it.isBlank() }
                    ?: error("Access token is empty in response")
                val refreshToken = result.refreshToken?.takeUnless { it.isBlank() }
                    ?: error("Refresh token is empty in response")

                userDataBase.userDao().upsert(user.toEntity())

                authStore.setAuthData(
                    userId = userId,
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )

                logger.i(
                    "AuthRepositoryImpl/isAuthed",
                    "Successfully checked. Tokens: $accessToken and $refreshToken"
                )

                emit(Result.success(Unit))
            } else {
                val errorMsg = "Not authorized (HTTP ${response.status.value})"
                logger.w("AuthRepositoryImpl/isAuthed", errorMsg)
                emit(Result.failure(Throwable(errorMsg)))
            }
        }.catch { e ->
            logger.e("AuthRepositoryImpl/isAuthed", "Exception: $e")
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

            val user = result.userDto ?: error("User data is missing in response")
            val userId = user.id?.toString() ?: error("User id is missing in response")
            val accessToken = result.accessToken?.takeUnless { it.isNullOrBlank() }
                ?: error("Access token is empty in response")
            val refreshToken = result.refreshToken?.takeUnless { it.isNullOrBlank() }
                ?: error("Refresh token is empty in response")

            userDataBase.userDao().upsert(user.toEntity())
            authStore.setAuthData(
                userId = userId,
                accessToken = accessToken,
                refreshToken = refreshToken
            )

            logger.i(
                "AuthRepositoryImpl/login",
                "Successful login. Tokens: $accessToken and $refreshToken"
            )

            emit(Result.success(Unit))
        } else {
            val errorMsg = "Ошибка входа (HTTP ${response.status.value})"
            logger.e("AuthRepositoryImpl/login", errorMsg)
            emit(Result.failure(Throwable(errorMsg)))
        }
    }.catch { e ->
        logger.e("AuthRepositoryImpl/login", "Exception: $e")
        emit(Result.failure(e))
    }
}

