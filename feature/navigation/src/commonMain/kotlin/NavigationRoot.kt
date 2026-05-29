import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import models.NavigationState
import models.UserProfile
import notifications.NotificationsScreen
import notifications.NotificationsViewModel
import org.koin.compose.viewmodel.koinViewModel
import participantProfile.ParticipantProfileScreen
import participantProfile.ParticipantProfileViewModel
import usersSearch.UsersSearchScreen
import usersSearch.UsersSearchViewModel
import utils.LocalHazeState

@Composable
internal fun NavigationRoot(
    modifier: Modifier = Modifier,
    uiState: NavigationState,
    handleIntent: (intent: NavigationIntent) -> Unit,
) {
    val rootBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.OnBoarding::class, Route.OnBoarding.serializer())
                    subclass(Route.Profile::class, Route.Profile.serializer())
                    subclass(Route.Auth::class, Route.Auth.serializer())
                    subclass(Route.Error::class, Route.Error.serializer())
                    subclass(Route.Projects::class, Route.Projects.serializer())
                    subclass(Route.Search::class, Route.Search.serializer())
                    subclass(Route.Participant::class, Route.Participant.serializer())
                    subclass(Route.Notifications::class, Route.Notifications.serializer())
                }
            }
        },
        Route.Auth,
    )
    var currentRoute by remember { mutableStateOf<Route>(Route.Auth) }
    fun navigateTo(route: Route) {
        currentRoute = route
        if (rootBackStack.lastOrNull() != route) {
            rootBackStack.add(route)
        }
    }

    var isUserDetermined by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isInitialCheckFinished, uiState.isAuthed, uiState.userProfile) {
        println("NavigationRoot: state changed. isInitialCheckFinished=${uiState.isInitialCheckFinished}, isAuthed=${uiState.isAuthed}, currentRoute=$currentRoute, profileFilled=${uiState.userProfile?.isProfileFilled}")
        if (uiState.isInitialCheckFinished) {
            if (uiState.isAuthed) {
                val isProfileFilled = uiState.isProfileFilledLocally || uiState.userProfile?.isProfileFilled == true
                println("NavigationRoot: deciding where to navigate. isProfileFilled=$isProfileFilled (local=${uiState.isProfileFilledLocally}, server=${uiState.userProfile?.isProfileFilled})")
                
                if (currentRoute is Route.Auth) {
                    rootBackStack.clear()
                    if (isProfileFilled) {
                        navigateTo(Route.Projects)
                    } else {
                        navigateTo(Route.OnBoarding)
                    }
                }
            } else if (currentRoute !is Route.Auth) {
                println("NavigationRoot: not authed, going to Auth")
                rootBackStack.clear()
                navigateTo(Route.Auth)
            }
            isUserDetermined = true
        }
    }

    val hazeState = rememberHazeState()
    CompositionLocalProvider(LocalHazeState provides hazeState) {
        Scaffold(
            bottomBar = {
                if (currentRoute !is Route.Auth && currentRoute !is Route.OnBoarding) {
                    AppNavigationBar(
                        navigateTo = ::navigateTo,
                        currentRoute = currentRoute,
                    )
                }
            },
        ) {
            Box {
                Background(enabled = true, useCirclesBackground = true, painter = null) {
                    if (!isUserDetermined) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        NavDisplay(
                            modifier = modifier,
                            backStack = rootBackStack,
                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator(),
                            ),
                            entryProvider = entryProvider {
                                entry<Route.Auth> {
                                    AuthNavigation(
                                        onAuth = {
                                            rootBackStack.clear()
                                            navigateTo(Route.OnBoarding)
                                        },
                                    )
                                }
                                entry<Route.OnBoarding> {
                                    OnBoardingNavigation(
                                        onFinished = { navigateTo(Route.Projects) },
                                    )
                                }
                                entry<Route.Profile> {
                                    ProfileNavigation(
                                        onLogout = { handleIntent(NavigationIntent.Logout) },
                                        onDeleteAccount = { handleIntent(NavigationIntent.DeleteAccount) },
                                    )
                                }
                                entry<Route.Projects> {
                                    ProjectsNavigation()
                                }
                                entry<Route.Search> {
                                    val vm: UsersSearchViewModel = koinViewModel()
                                    UsersSearchScreen(
                                        vm = vm,
                                        onUserClick = { user: UserProfile ->
                                            navigateTo(Route.Participant(user.id))
                                        },
                                    )
                                }
                                entry<Route.Participant> { key ->
                                    val vm: ParticipantProfileViewModel = koinViewModel()
                                    ParticipantProfileScreen(
                                        userId = key.userId,
                                        onBack = { rootBackStack.removeLastOrNull() },
                                        vm = vm,
                                    )
                                }
                                entry<Route.Notifications> {
                                    val vm: NotificationsViewModel = koinViewModel()
                                    NotificationsScreen(vm = vm)
                                }
                            },
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalHazeApi::class)
@Composable
private fun Background(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    painter: Painter? = null,
    useCirclesBackground: Boolean = true,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val hazeState = LocalHazeState.current

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().hazeSource(hazeState)) {
            if (enabled && useCirclesBackground) {
                MovingCirclesBackground(isDarkTheme = isDarkTheme)
            } else if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize().hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    blurRadius = 20.dp,
                    noiseFactor = 0f,
                    tint = HazeTint(
                        color = if (isDarkTheme) Color.Black.copy(alpha = 0.05f)
                        else Color.White.copy(alpha = 0.05f),
                    ),
                ),
            ),
        )

        Box(modifier = modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
private fun MovingCirclesBackground(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_bg")

    val x1 by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(18000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c1_x",
    )
    val y1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(13000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c1_y",
    )

    val x2 by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(tween(22000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c2_x",
    )
    val y2 by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(tween(16000, easing = EaseInOut), RepeatMode.Reverse),
        label = "c2_y",
    )

    val color1 =
        (if (!isDarkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryFixedDim)
            .copy(alpha = 0.5f)
    val color2 =
        (if (!isDarkTheme) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiaryFixedDim)
            .copy(alpha = 0.4f)
    val bgColor = MaterialTheme.colorScheme.background

    Canvas(modifier = modifier.fillMaxSize().background(bgColor)) {
        val radius1 = size.minDimension * 0.7f
        val radius2 = size.minDimension * 0.6f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color1, Color.Transparent),
                center = Offset(size.width * x1, size.height * y1),
                radius = radius1,
            ),
            center = Offset(size.width * x1, size.height * y1),
            radius = radius1,
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color2, Color.Transparent),
                center = Offset(size.width * x2, size.height * y2),
                radius = radius2,
            ),
            center = Offset(size.width * x2, size.height * y2),
            radius = radius2,
        )
    }
}
