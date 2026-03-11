package repositories

import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import local.datastore.TokenStore
import remote.apis.AuthApi
import remote.apis.dtos.responses.RegisterResponseBodyDto
import utils.Logger

class AuthRepositoryImpl(
    private val tokenStore: TokenStore,
    private val authApi: AuthApi,
    private val logger: Logger
) : AuthRepository {
    override fun register(username: String, email: String, password: String): Flow<Result<Unit>> =
        flow {
            logger.i(
                "AuthRepositoryImpl/register",
                "Started registration.\nusername: $username\nemail:$email\npassword:$password"
            )

            val response = authApi.register(
                username = username,
                email = email,
                password = password
            )

            if (response.status.value in 200..299) {
                val result = response.body<RegisterResponseBodyDto>()

                val accessToken = result.accessToken.takeUnless { it.isBlank() }
                    ?: error("Access token is empty in response")
                val refreshToken = result.refreshToken.takeUnless { it.isBlank() }
                    ?: error("Refresh token is empty in response")

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
                    "AuthRepositoryImpl/register",
                    "Client error"
                )

                emit(Result.failure(Throwable("АШИБКА")))
            }
        }.catch { e ->
            logger.e("AuthRepositoryImpl/register", "Exception: $e")
            emit(Result.failure(e))
        }
}

