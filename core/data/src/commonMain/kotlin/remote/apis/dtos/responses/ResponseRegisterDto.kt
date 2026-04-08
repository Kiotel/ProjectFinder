package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import remote.apis.dtos.common.UserDto

@Serializable
internal data class ResponseRegisterDto(
    @Serializable @SerialName("accessToken") val accessToken: String,
    @Serializable @SerialName("refreshToken") val refreshToken: String,
    @Serializable @SerialName("user") val userDto: UserDto,
)
