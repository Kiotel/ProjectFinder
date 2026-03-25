package login.models

import androidx.compose.runtime.Stable

@Stable
internal data class LoginFormData(
    val email: String,
    val password: String,
)