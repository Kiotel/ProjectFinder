package remote.apis.dtos.responses

import kotlinx.serialization.Serializable
import remote.apis.dtos.common.ProjectDto

@Serializable
internal data class ResponseProjectsDto(
    val projects: List<ProjectDto>
)

