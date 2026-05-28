package remote.apis.dtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import remote.apis.dtos.common.ContactDto
import remote.apis.dtos.common.SkillDto

@Serializable
internal data class RequestUpdateUserBodyDto(
    @SerialName("firstName") val firstName: String? = null,
    @SerialName("lastName") val lastName: String? = null,
    @SerialName("age") val age: Int? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("university") val university: String? = null,
    @SerialName("faculty") val faculty: String? = null,
    @SerialName("programCode") val programCode: String? = null,
    @SerialName("studyMode") val studyMode: String? = null,
    @SerialName("schedule") val schedule: String? = null,
    @SerialName("contacts") val contacts: List<ContactDto>? = null,
    @SerialName("goals") val goals: String? = null,
    @SerialName("qualities") val qualities: List<String>? = null,
    @SerialName("skills") val skills: List<SkillDto>? = null,
    @SerialName("interests") val interests: List<String>? = null,
    @SerialName("portfolioUrl") val portfolioUrl: String? = null,
)
