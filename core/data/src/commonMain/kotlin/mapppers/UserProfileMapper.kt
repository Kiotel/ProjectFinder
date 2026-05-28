package mapppers

import local.database.entities.UserEntity
import models.Contact
import models.Skill
import models.UserProfile
import remote.apis.dtos.common.ContactDto
import remote.apis.dtos.common.SkillDto
import remote.apis.dtos.requests.RequestUpdateUserBodyDto
import remote.apis.dtos.responses.ResponseUserDto
import kotlin.time.Clock

internal fun ResponseUserDto.toDomain(): UserProfile = UserProfile(
    id = id?.toString() ?: "0",
    username = username.orEmpty(),
    email = email.orEmpty(),
    firstName = firstName,
    lastName = lastName,
    displayName = fullName ?: listOfNotNull(firstName, lastName).joinToString(" ")
        .ifBlank { username.orEmpty() },
    age = age,
    city = city ?: location,
    university = university ?: universityHeader,
    faculty = faculty,
    programCode = programCode,
    studyMode = studyMode,
    schedule = schedule ?: ageDisplay, // Fallback to ageDisplay if schedule is missing
    contacts = contacts.orEmpty().map { it.toDomain() },
    avatarUrl = avatarUrl,
    skills = skills.orEmpty().mapNotNull { it.toDomain() },
    interests = interests.orEmpty(),
    goals = goals,
    qualities = qualities.orEmpty(),
    portfolioUrl = portfolioUrl,
    createdAt = createdAt,
)

internal fun ResponseUserDto.toEntity() = UserEntity(
    id = id?.toString() ?: "0",
    userName = username.orEmpty(),
    email = email.orEmpty(),
    fullName = fullName ?: listOfNotNull(firstName, lastName).joinToString(" ").ifBlank { null },
    firstName = firstName,
    university = university ?: universityHeader,
    avatarUrl = avatarUrl,
    lastUpdated = Clock.System.now().toEpochMilliseconds(),
)

private fun ContactDto.toDomain() = Contact(type = type, value = value)

private fun SkillDto.toDomain(): Skill? {
    val skillName = name?.takeIf { it.isNotBlank() } ?: return null
    return Skill(name = skillName, level = level)
}

/** Тело PUT /users/{id} — только непустые поля. */
internal fun UserProfile.toUpdateRequest(): RequestUpdateUserBodyDto = RequestUpdateUserBodyDto(
    firstName = firstName?.takeIf { it.isNotBlank() },
    lastName = lastName?.takeIf { it.isNotBlank() },
    age = age,
    city = city?.takeIf { it.isNotBlank() },
    university = university?.takeIf { it.isNotBlank() },
    faculty = faculty?.takeIf { it.isNotBlank() },
    programCode = programCode?.takeIf { it.isNotBlank() },
    studyMode = studyMode?.takeIf { it.isNotBlank() },
    schedule = schedule?.takeIf { it.isNotBlank() },
    contacts = contacts.takeIf { it.isNotEmpty() }
        ?.map { ContactDto(type = it.type, value = it.value) },
    goals = goals?.takeIf { it.isNotBlank() },
    qualities = qualities.takeIf { it.isNotEmpty() },
    skills = skills.takeIf { it.isNotEmpty() }
        ?.map { SkillDto(name = it.name, level = it.level) },
    interests = interests.takeIf { it.isNotEmpty() },
    portfolioUrl = portfolioUrl?.takeIf { it.isNotBlank() },
)
