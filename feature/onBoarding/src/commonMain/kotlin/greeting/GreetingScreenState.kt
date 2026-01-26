package greeting

data class GreetingScreenState(
    val isLoading: Boolean,
    val errorMsg: String?,
    val isFilledDescriptionForm: Boolean,
    val firstTime: Boolean,
)