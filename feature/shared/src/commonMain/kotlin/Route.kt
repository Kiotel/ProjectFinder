import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @kotlinx.serialization.Serializable
    data object OnBoarding: Route {
        @kotlinx.serialization.Serializable
        data object Greeting: Route
        @kotlinx.serialization.Serializable
        data object DescriptionForm: Route
    }
    @kotlinx.serialization.Serializable
    data object Auth: Route {
        @kotlinx.serialization.Serializable
        data object Login: Route
        @kotlinx.serialization.Serializable
        data object Registration: Route
        @kotlinx.serialization.Serializable
        data object PasswordReset: Route
    }
    @kotlinx.serialization.Serializable
    data object Error: Route{
        @kotlinx.serialization.Serializable
        data object UnknownError: Route
    }
}