package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseProjectLikeDto(
    @Serializable @SerialName("liked") val liked: Boolean?
)
