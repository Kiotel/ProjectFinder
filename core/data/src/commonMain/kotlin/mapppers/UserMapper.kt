package mapppers

import local.database.entities.UserEntity
import models.User
import remote.apis.dtos.common.UserDto
import kotlin.time.Clock
import kotlin.time.Clock.System
import kotlin.time.Instant

fun UserDto.toEntity() = UserEntity(
    id = this.id,
    userName = this.username,
    email = this.email,
    fullName = this.fullName,
    avatarUrl = this.avatarUrl,
    lastUpdated = System.now().toEpochMilliseconds()
)

fun UserDto.toDomain() = User(
    id = this.id,
    userName = this.username,
    email = this.email,
    fullName = this.fullName,
    avatarUrl = this.avatarUrl
)

fun UserEntity.toDomain() = User(
    id = this.id,
    userName = this.userName,
    email = this.email,
    fullName = this.fullName,
    avatarUrl = this.avatarUrl
)