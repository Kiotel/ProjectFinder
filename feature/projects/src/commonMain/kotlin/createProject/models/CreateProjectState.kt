package createProject.models

internal data class CreateProjectState(
    val title: String = "",
    val description: String = "",
    val industry: String = "",
    val roles: List<Pair<String, Int>> = listOf("Разработчик" to 1),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
