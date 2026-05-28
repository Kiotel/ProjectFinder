package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestProjectResponseBodyDto(
    @SerialName("projectId") val projectId: Int,
    @SerialName("message") val message: String,
)
