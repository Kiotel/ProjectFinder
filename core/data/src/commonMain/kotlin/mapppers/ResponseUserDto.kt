package mapppers

import io.ktor.http.cio.Response
import local.database.entities.UserEntity
import models.User
import remote.apis.dtos.common.UserDto

internal fun Response.toEntity() = UserEntity(
)

internal fun UserDto.toDomain() = User(
    id = this.id ?: "no id",
    userName = this.username ?: "no username",
    email = this.email ?: "no email",
    fullName = this.fullName,
    avatarUrl = this.avatarUrl
)

internal fun UserEntity.toDomain() = User(
    id = this.id,
    userName = this.userName,
    email = this.email,
    fullName = this.fullName,
    avatarUrl = this.avatarUrl
)