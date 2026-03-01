package greeting

internal data class GreetingScreenState(
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
)