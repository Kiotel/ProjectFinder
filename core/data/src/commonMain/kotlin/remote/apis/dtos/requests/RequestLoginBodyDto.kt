package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestLoginBodyDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)
