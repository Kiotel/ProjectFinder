package valera.app.projectfinder

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinApplicationPreview
import org.koin.dsl.koinConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            KoinApplication(
                configuration = koinConfiguration(declaration = { modules(appModule) })
            ) {
                App()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    KoinApplicationPreview(application = { modules(appModule) }) {
        App()
    }
}