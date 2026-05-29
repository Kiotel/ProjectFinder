package models

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class ProjectMember(
    val userId: String,
    val username: String,
    val firstName: String,
    val avatarUrl: String?,
    val roleName: String? = null,
)
