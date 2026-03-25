package login

import AuthViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier, vm: LoginViewModel, svm: AuthViewModel, goToRegister: () -> Unit
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    ScreenLayout(
        modifier = modifier,
    ) { innerPadding ->
        Button(
            modifier = Modifier.padding(innerPadding),
            onClick = goToRegister,
            content = {
                Text("Зарегистрироваться")
            }
        )
    }
}


