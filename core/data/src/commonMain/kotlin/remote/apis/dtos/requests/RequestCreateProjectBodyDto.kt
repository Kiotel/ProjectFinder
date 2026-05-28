package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import remote.apis.dtos.common.ProjectRoleDto

@Serializable
internal data class RequestCreateProjectBodyDto(
    @SerialName("authorId") val authorId: Int? = null,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("status") val status: String? = "idea",
    @SerialName("industry") val industry: String? = null,
    @SerialName("roles") val roles: List<ProjectRoleDto>? = null,
)
