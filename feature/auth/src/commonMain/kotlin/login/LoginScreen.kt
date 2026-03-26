package login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.BrandTitle
import components.RegistrationTextInput
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import login.models.LoginFormData
import login.models.LoginState
import modifiers.cheapGlassEffect
import modifiers.glassEffect
import utils.LocalHazeState
import utils.SnackBarManager

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginState,
    snackBarManager: SnackBarManager,
    handleIntent: (intent: LoginIntent) -> Unit,
    goToRegister: () -> Unit,
) {

    val scrollState = rememberScrollState()
    ScreenLayout(
        modifier = modifier,
        snackBarManager = snackBarManager,
    ) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding() + 36.dp).imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BrandTitle()
            LoginForm(
                modifier = Modifier.padding(12.dp).background(Color.Transparent)
                    .glassEffect(
                        LocalHazeState.current,
                        shape = MaterialTheme.shapes.extraLarge,
                        fillAlpha = 0.3f,
                        borderAlpha = 0.4f,
                        borderWidth = 2.dp,
                        blurRadius = 10.dp
                    ),
                data = LoginFormData(
                    email = uiState.email,
                    password = uiState.password,
                ),
                onEmailChange = { handleIntent(LoginIntent.SetEmail(it)) },
                onPasswordChange = { handleIntent(LoginIntent.SetPassword(it)) },
                onLogin = { handleIntent(LoginIntent.OnLogin) })
            OutlinedButton(
                modifier = Modifier.cheapGlassEffect(ButtonDefaults.outlinedShape),
                colors = ButtonDefaults.outlinedButtonColors().copy(
                    containerColor = Color.Transparent,
                ),
                border = null,
                onClick = goToRegister,
            ) {
                Text(text = "Зарегистрироваться", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun LoginForm(
    modifier: Modifier = Modifier,
    data: LoginFormData,
    onEmailChange: (newEmail: String) -> Unit,
    onPasswordChange: (newPassword: String) -> Unit,
    onLogin: () -> Unit
) {
    val passwordFocus = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Авторизация",
                modifier = Modifier.padding(top = 18.dp),
                style = MaterialTheme.typography.titleLargeEmphasized,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )
            RegistrationTextInput(
                labelText = "Почта",
                value = data.email,
                onValueChange = { onEmailChange(it) },
                keyboardType = KeyboardType.Email,
                onNext = { passwordFocus.requestFocus(focusDirection = FocusDirection.Up) },
            )
            RegistrationTextInput(
                isSecret = true,
                labelText = "Пароль",
                value = data.password,
                onValueChange = {
                    onPasswordChange(it)
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onNext = {
                    focusManager.clearFocus()
                    onLogin()
                },
                focusRequester = passwordFocus
            )
            Button(
                modifier = Modifier.padding(bottom = 24.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                ),
                onClick = {
                    focusManager.clearFocus()
                    onLogin()
                },
            ) {
                Text(text = "Войти", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
