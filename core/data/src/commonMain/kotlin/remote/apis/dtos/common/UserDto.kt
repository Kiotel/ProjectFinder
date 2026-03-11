package remote.apis.dtos.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: Long,
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("fullName") val fullName: String?,
    @SerialName("avatarUrl") val avatarUrl: String?
)
