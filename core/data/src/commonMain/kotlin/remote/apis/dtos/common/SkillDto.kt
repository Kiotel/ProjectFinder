package remote.apis.dtos.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SkillDto(
    @SerialName("name") val name: String? = null,
    @SerialName("level") val level: String? = null,
)
