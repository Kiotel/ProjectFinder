package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestCommentBodyDto(
    @SerialName("content") val content: String,
)
