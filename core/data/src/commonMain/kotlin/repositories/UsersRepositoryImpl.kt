package repositories

import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import local.database.UserDataBase
import local.secureStore.AuthStore
import mapppers.toDomain
import models.User
import remote.apis.BackendApi
import remote.apis.dtos.responses.ResponseUserDto
import utils.Logger

internal class UsersRepositoryImpl(
    private val backendApi: BackendApi,
    private val authStore: AuthStore,
    private val userDataBase: UserDataBase,
    private val logger: Logger
) : UsersRepository {
    override fun getCurrentUserInfo(): Flow<Result<User>> = flow {
        try {
            val localUser = userDataBase.userDao().get(authStore.userId)
            if (localUser != null) emit(Result.success(localUser.toDomain()))

            val apiResult = backendApi.getUserById(authStore.userId)
            if (!apiResult.status.isSuccess()) {
                error("StatusCode: ${apiResult.status.value}. Couldn't get current user with id: ${authStore.userId}. ServerResponse: $apiResult ")
            }
            val remoteUser = apiResult.body<ResponseUserDto>()

            userDataBase.userDao().upsert(remoteUser.to)

        } catch (e: Throwable) {
            logger.e("UserRepositoryImpl/getCurrentUserInfo", e.stackTraceToString())
            emit(Result.failure(e))
        }
    }

    override fun getUserById(id: String): Flow<Result<User>> {
        TODO("Not yet implemented")
    }

    override fun getUsers(): Flow<Result<List<User>>> {
        TODO("Not yet implemented")
    }
}

