package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import remote.apis.dtos.common.SkillDto

@Serializable
internal class ResponseUserDto(
    @SerialName("id") val id: Int,
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("firstName") val firstName: String?,
    @SerialName("lastName") val lastName: String?,
    @SerialName("avatarUrl") val avatarUrl: String?,
    // !TODO: СДелать скиллы и т.д через сериализацию
    @SerialName("skills") val skills: List<SkillDto>,
    @SerialName("interests") val interests: List<String>,
    @SerialName("goals") val goals: String?,
    @SerialName("portfolioUrl") val portfolioUrl: String?,
    @SerialName("createAt") val createdAt: String?
)