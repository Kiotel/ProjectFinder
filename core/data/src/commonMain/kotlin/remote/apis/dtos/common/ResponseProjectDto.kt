package remote.apis.dtos.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseProjectDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("authorId") val authorId: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("briefDescription") val briefDescription: String? = null,
    @SerialName("stage") val stage: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("industry") val industry: String? = null,
    @SerialName("deadline") val deadline: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @SerialName("viewsCount") val viewsCount: Int? = null,
    @SerialName("likesCount") val likesCount: Int? = null,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("roles") val roles: List<ProjectRoleDto>? = null,
    @SerialName("neededRoles") val neededRoles: List<ProjectRoleDto>? = null,
    @SerialName("authorName") val authorName: String? = null,
    @SerialName("isActive") val isActive: Boolean? = null,
)

@Serializable
internal data class ProjectRoleDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("projectId") val projectId: Int? = null,
    @SerialName("roleName") val roleName: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("spotsTotal") val spotsTotal: Int? = null,
    @SerialName("spotsFilled") val spotsFilled: Int? = null,
    @SerialName("requiredSkills") val requiredSkills: String? = null, // Changed from List<String>
    @SerialName("isFilled") val isFilled: Boolean? = null,
    @SerialName("createdAt") val createdAt: String? = null,
)
