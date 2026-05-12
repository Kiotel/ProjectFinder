package remote.apis.dtos.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseProjectDto(
    @SerialName("id") val id: String,
    @SerialName("authorId") val authorId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("briefDescription") val briefDescription: String,
    @SerialName("stage") val stage: String,
    @SerialName("status") val status: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
    @SerialName("viewsCount") val viewsCount: Int,
    @SerialName("likesCount") val likesCount: Int,
    @SerialName("tags") val tags: List<String>,
    @SerialName("neededRoles") val neededRoles: List<NeededRole>,
    @SerialName("authorName") val authorName: String,
)

@Serializable
internal data class NeededRole(
    @SerialName("id") val id: Int,
    @SerialName("projectId") val projectId: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("requiredSkills") val requiredSkills: List<String>,
    @SerialName("isFilled") val isFilled: Boolean,
    @SerialName("createdAt") val createdAt: String,
)