package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestRefreshAuthTokenBodyDto(
    @SerialName("refreshToken") val refreshToken: String,
)
