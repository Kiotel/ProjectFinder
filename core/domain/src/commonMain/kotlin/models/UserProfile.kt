package models

data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val displayName: String,
    val age: Int? = null,
    val city: String? = null,
    val university: String? = null,
    val faculty: String? = null,
    val programCode: String? = null,
    val studyMode: String? = null,
    val schedule: String? = null,
    val contacts: List<Contact> = emptyList(),
    val avatarUrl: String? = null,
    val skills: List<Skill> = emptyList(),
    val interests: List<String> = emptyList(),
    val goals: String? = null,
    val qualities: List<String> = emptyList(),
    val portfolioUrl: String? = null,
    val createdAt: String? = null,
) {
    val isProfileFilled: Boolean
        get() = !firstName.isNullOrBlank()
}

data class Contact(
    val type: String,
    val value: String,
)

data class Skill(
    val name: String,
    val level: String? = null,
)

data class ProjectComment(
    val id: String,
    val projectId: String,
    val authorId: String?,
    val authorName: String?,
    val content: String,
    val createdAt: String?,
)

data class ProjectApplicant(
    val responseId: String,
    val userId: String,
    val username: String,
    val message: String?,
    val status: String,
)

data class PagedUsers(
    val users: List<UserProfile>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
)
