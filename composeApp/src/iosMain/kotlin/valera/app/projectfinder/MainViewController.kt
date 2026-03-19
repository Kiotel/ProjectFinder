package valera.app.projectfinder

import App
import androidx.compose.ui.window.ComposeUIViewController
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

fun MainViewController() = ComposeUIViewController {
    KoinApplication(
        configuration = koinConfiguration(declaration = {
            modules(appModule)
        })
    ) {
    App()
    }
}