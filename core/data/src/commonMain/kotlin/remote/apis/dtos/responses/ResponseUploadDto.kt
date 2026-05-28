package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseUploadDto(
    @SerialName("message") val message: String? = null,
    @SerialName("url") val url: String? = null,
)
