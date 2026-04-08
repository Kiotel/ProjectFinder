package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestRefreshAuthTokenBodyDto(
    @SerialName("refreshToken") val refreshToken: String,
)
