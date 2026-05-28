package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import remote.apis.dtos.common.ContactDto
import remote.apis.dtos.common.SkillDto

@Serializable
internal data class ResponseUserDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("firstName") val firstName: String? = null,
    @SerialName("lastName") val lastName: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null,
    @SerialName("age") val age: Int? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("university") val university: String? = null,
    @SerialName("faculty") val faculty: String? = null,
    @SerialName("programCode") val programCode: String? = null,
    @SerialName("studyMode") val studyMode: String? = null,
    @SerialName("schedule") val schedule: String? = null,
    @SerialName("contacts") val contacts: List<ContactDto>? = null,
    @SerialName("skills") val skills: List<SkillDto>? = null,
    @SerialName("interests") val interests: List<String>? = null,
    @SerialName("goals") val goals: String? = null,
    @SerialName("qualities") val qualities: List<String>? = null,
    @SerialName("portfolioUrl") val portfolioUrl: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,

    // Fields from UserProfileResponse (Design/Glassmorphism)
    @SerialName("fullName") val fullName: String? = null,
    @SerialName("universityHeader") val universityHeader: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("ageDisplay") val ageDisplay: String? = null,
    @SerialName("isOnline") val isOnline: Boolean? = null,
    @SerialName("isNew") val isNew: Boolean? = null,
    @SerialName("stats") val stats: UserStatsDto? = null,
)

@Serializable
internal data class UserStatsDto(
    @SerialName("views") val views: Int? = 0,
    @SerialName("saved") val saved: Int? = 0,
    @SerialName("projects") val projects: Int? = 0,
)
