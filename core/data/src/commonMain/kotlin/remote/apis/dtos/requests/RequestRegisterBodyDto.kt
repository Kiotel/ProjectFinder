package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestRegisterBodyDto(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)
