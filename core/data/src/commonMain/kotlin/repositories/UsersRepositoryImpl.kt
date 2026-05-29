package repositories

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import local.secureStore.AuthStore
import mapppers.toDomain
import mapppers.toUpdateRequest
import models.PagedUsers
import models.UserProfile
import remote.apis.BackendApi
import remote.apis.dtos.responses.ResponseUserDto
import remote.apis.dtos.responses.ResponseUsersPageDto
import utils.Logger
import utils.endpointUnavailableMessage
import utils.httpErrorMessage

internal class UsersRepositoryImpl(
    private val backendApi: BackendApi,
    private val authStore: AuthStore,
    private val logger: Logger,
) : UsersRepository {

    override fun getCurrentUserProfile(): Flow<Result<UserProfile>> = flow {
        if (authStore.userId.isBlank()) {
            emit(Result.failure(Exception(httpErrorMessage(401, ""))))
            return@flow
        }
        loadUser(authStore.userId, syncUserId = true).collect { emit(it) }
    }

    override fun getUserById(id: String): Flow<Result<UserProfile>> = flow {
        loadUser(id).collect { emit(it) }
    }

    override fun searchUsers(query: String, page: Int, limit: Int): Flow<Result<PagedUsers>> = flow {
        try {
            if (authStore.accessToken.isBlank()) {
                emit(Result.failure(Exception(httpErrorMessage(401, ", чтобы искать участников"))))
                return@flow
            }
            val response = backendApi.searchUsers(query = query, page = page, limit = limit)
            if (!response.status.isSuccess()) {
                val errorMsg = httpErrorMessage(response.status.value, " при поиске")
                logger.e("UsersRepositoryImpl/searchUsers", "HTTP ${response.status.value}: $errorMsg")
                if (response.status.value == 404) {
                    emit(Result.failure(Exception(endpointUnavailableMessage("Поиск участников"))))
                } else {
                    emit(Result.failure(Exception(errorMsg)))
                }
                return@flow
            }
            val body = parseUsersPageResponse(response)
            val users = body.data.map { it.toDomain() }
            val pagination = body.pagination
            emit(
                Result.success(
                    PagedUsers(
                        users = users,
                        currentPage = pagination?.currentPage ?: page,
                        totalPages = pagination?.totalPages ?: 1,
                        totalItems = pagination?.totalItems ?: users.size,
                    ),
                ),
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.e("UsersRepositoryImpl/searchUsers", e.stackTraceToString())
            emit(Result.failure(e))
        }
    }

    override suspend fun updateProfile(profile: UserProfile): Result<Unit> = runCatching {
        if (authStore.accessToken.isBlank()) {
            error(httpErrorMessage(401, ""))
        }
        val userId = authStore.userId.takeIf { it.isNotBlank() }
            ?: profile.id.takeIf { it.isNotBlank() && it != "0" }
            ?: error(httpErrorMessage(401, ""))
        val response = backendApi.updateUser(userId, profile.toUpdateRequest())
        if (!response.status.isSuccess()) {
            val bodyText = try { response.body<String>() } catch (_: Exception) { "" }
            val errorMsg = when (response.status.value) {
                500 -> "Сервер не смог сохранить анкету (ошибка 500). Попробуйте позже."
                else -> httpErrorMessage(response.status.value, " при сохранении профиля: $bodyText")
            }
            logger.e("UsersRepositoryImpl/updateProfile", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
    }

    override suspend fun bookmarkUser(userId: String): Result<Unit> = runCatching {
        if (authStore.accessToken.isBlank()) {
            error(httpErrorMessage(401, ""))
        }
        val response = backendApi.bookmarkUser(userId)
        if (!response.status.isSuccess()) {
            val errorMsg = when (response.status.value) {
                404 -> endpointUnavailableMessage("Избранное")
                else -> httpErrorMessage(response.status.value, " при добавлении в избранное")
            }
            logger.e("UsersRepositoryImpl/bookmarkUser", "HTTP ${response.status.value}: $errorMsg")
            error(errorMsg)
        }
    }

    private fun loadUser(id: String, syncUserId: Boolean = false): Flow<Result<UserProfile>> = flow {
        try {
            val response = backendApi.getUserById(id)
            if (!response.status.isSuccess()) {
                val errorMsg = httpErrorMessage(response.status.value, " при загрузке профиля")
                logger.e("UsersRepositoryImpl/loadUser", "HTTP ${response.status.value}: $errorMsg")
                emit(Result.failure(Exception(errorMsg)))
                return@flow
            }
            val profile = parseUserResponse(response)
            if (syncUserId && profile.id.isNotBlank() && profile.id != "0") {
                authStore.userId = profile.id
            }
            emit(Result.success(profile))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.e("UsersRepositoryImpl/loadUser", e.stackTraceToString())
            emit(Result.failure(e))
        }
    }

    private suspend fun parseUserResponse(response: HttpResponse): UserProfile {
        val text = response.bodyAsText()
        return try {
            backendApi.json.decodeFromString<ResponseUserDto>(text).toDomain()
        } catch (_: Exception) {
            try {
                val page = backendApi.json.decodeFromString<ResponseUsersPageDto>(text)
                page.data.firstOrNull()?.toDomain()
                    ?: error("Пустой ответ профиля")
            } catch (e: Exception) {
                logger.e("UsersRepositoryImpl/parseUserResponse", "Failed to parse user: ${e.message}")
                throw e
            }
        }
    }

    private suspend fun parseUsersPageResponse(response: HttpResponse): ResponseUsersPageDto {
        // Читаем body один раз, чтобы избежать ошибки «Body is consumed»
        val text = response.bodyAsText()
        return try {
            backendApi.json.decodeFromString<ResponseUsersPageDto>(text)
        } catch (_: Exception) {
            try {
                val list = backendApi.json.decodeFromString<List<ResponseUserDto>>(text)
                ResponseUsersPageDto(data = list)
            } catch (e: Exception) {
                logger.e("UsersRepositoryImpl/parseUsersPageResponse", "Failed to parse users: ${e.message}")
                throw e
            }
        }
    }
}
