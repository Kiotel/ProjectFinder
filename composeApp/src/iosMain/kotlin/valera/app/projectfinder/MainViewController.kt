package valera.app.projectfinder

import App
import androidx.compose.ui.window.ComposeUIViewController
import local.secureStore.ProfileFillStore
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.dsl.koinConfiguration

fun MainViewController() = ComposeUIViewController {
    KoinApplication(
        configuration = koinConfiguration(declaration = {
            modules(appModule)
        })
    ) {
        koinInject<ProfileFillStore>() // Force Koin to create ProfileFillStore → sync to ProfileFillManager
        App()
    }
}