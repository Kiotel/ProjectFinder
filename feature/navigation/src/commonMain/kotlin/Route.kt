import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object OnBoarding: Route {
        @Serializable
        data object Greeting: Route
        @Serializable
        data object DescriptionForm: Route
    }
    @Serializable
    data object Auth: Route {
        @Serializable
        data object Login: Route
        @Serializable
        data object Registration: Route
        @Serializable
        data object PasswordReset: Route
    }
    @Serializable
    data object Error: Route{
        @Serializable
        data object UnknownError: Route
    }
}