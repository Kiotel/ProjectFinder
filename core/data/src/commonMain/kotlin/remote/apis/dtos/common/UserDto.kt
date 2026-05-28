package remote.apis.dtos.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserDto(
    @SerialName("id") val id: Int?,
    @SerialName("username") val username: String?,
    @SerialName("email") val email: String?,
    @SerialName("firstName") val firstName: String? = null,
    @SerialName("lastName") val lastName: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String?,
)
