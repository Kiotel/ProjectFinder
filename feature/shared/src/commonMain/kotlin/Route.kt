import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import models.Project

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object OnBoarding : Route {
        @Serializable
        data object Greeting : Route

        @Serializable
        data object Description : Route
    }

    @Serializable
    data object Auth : Route {
        @Serializable
        data object Registration : Route

        @Serializable
        data object Login : Route

        @Serializable
        data object PasswordReset : Route
    }

    @Serializable
    data object Profile : Route {
        @Serializable
        data object Detailed : Route
    }

    @Serializable
    data object Projects : Route {
        @Serializable
        data object AllProjects : Route

        @Serializable
        data class DetailedProject(val project: Project) : Route
    }

    @Serializable
    data object Error : Route {
        @Serializable
        data object UnknownError : Route
    }
}