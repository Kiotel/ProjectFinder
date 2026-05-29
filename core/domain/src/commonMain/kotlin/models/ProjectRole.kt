package models

import kotlinx.serialization.Serializable

@Serializable
data class ProjectRole(
    val name: String,
    val spots: Int,
    val skills: List<String> = emptyList(),
)
