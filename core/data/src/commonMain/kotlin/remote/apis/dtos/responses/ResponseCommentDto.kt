package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseCommentDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("projectId") val projectId: Int? = null,
    @SerialName("userId") val userId: Int? = null,
    @SerialName("authorId") val authorId: Int? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("authorName") val authorName: String? = null,
)
