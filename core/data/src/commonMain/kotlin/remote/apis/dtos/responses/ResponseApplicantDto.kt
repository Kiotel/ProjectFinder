package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseApplicantDto(
    @SerialName("responseId") val responseId: Int,
    @SerialName("userId") val userId: Int,
    @SerialName("username") val username: String,
    @SerialName("message") val message: String? = null,
    @SerialName("status") val status: String,
)
