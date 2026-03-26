package models

data class User(
    val id: String,
    val userName: String,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?
)