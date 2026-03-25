package registration

import AuthViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.BrandTitle
import components.RegistrationTextInput
import components.ScreenLayout
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.rememberHazeState
import modifiers.glassEffect
import org.jetbrains.compose.resources.stringResource
import registration.models.RegistrationData
import utils.AgreementLink
import utils.LocalHazeState

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun RegistrationScreen(
    modifier: Modifier = Modifier, vm: RegistrationViewModel, svm: AuthViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    ScreenLayout(
        modifier = modifier,
        snackBarId = uiState.currentSnackBarMessageId,
        snackBarText = if (uiState.snackBarMessageResource != null) {
            stringResource(
                uiState.snackBarMessageResource!!
            )
        } else {
            null
        },
        snackBarDuration = SnackbarDuration.Long
    ) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding() + 36.dp).imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BrandTitle()
            RegistrationForm(
                modifier = Modifier.padding(12.dp).background(Color.Transparent)
                .glassEffect(
                    LocalHazeState.current,
                    shape = MaterialTheme.shapes.extraLarge,
                    fillAlpha = 0.3f,
                    borderAlpha = 0.4f,
                    borderWidth = 2.dp,
                    blurRadius = 10.dp
                ),
                data = RegistrationData(
                    email = uiState.email,
                    emailErrorText = uiState.emailErrorText?.let { stringResource(it) },
                    login = uiState.login,
                    loginErrorText = uiState.loginErrorText?.let { stringResource(it) },
                    password = uiState.password,
                    passwordErrorText = uiState.passwordErrorText?.let { stringResource(it) },
                    passwordCopy = uiState.passwordCopy,
                    passwordCopyErrorText = uiState.passwordCopyErrorText?.let { stringResource(it) },
                    consent = uiState.consent,
                    consentErrorText = uiState.consentErrorText?.let { stringResource(it) },
                    registrationErrorText = uiState.snackBarMessageResource?.let { stringResource(it) }),
                onEmailChange = { vm.handleIntent(RegistrationIntent.SetEmail(it)) },
                onLoginChange = { vm.handleIntent(RegistrationIntent.SetLogin(it)) },
                onPasswordChange = { vm.handleIntent(RegistrationIntent.SetPassword(it)) },
                onPasswordCopyChange = { vm.handleIntent(RegistrationIntent.SetPasswordCopy(it)) },
                onConsentChange = { vm.handleIntent(RegistrationIntent.SetConsent(it)) },
                onRegistration = { vm.handleIntent(RegistrationIntent.OnRegister) })
        }
    }
}

@Composable
private fun RegistrationForm(
    modifier: Modifier = Modifier,
    data: RegistrationData,
    onEmailChange: (newEmail: String) -> Unit,
    onLoginChange: (newLogin: String) -> Unit,
    onPasswordChange: (newPassword: String) -> Unit,
    onPasswordCopyChange: (newPasswordCopy: String) -> Unit,
    onConsentChange: (newConsent: Boolean) -> Unit,
    onRegistration: () -> Unit
) {
    val loginFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val passwordCopyFocus = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Регистрация",
                modifier = Modifier.padding(top = 18.dp),
                style = MaterialTheme.typography.titleLargeEmphasized,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )
            RegistrationTextInput(
                errorText = data.emailErrorText,
                isError = !data.emailErrorText.isNullOrBlank(),
                labelText = "Почта",
                value = data.email,
                onValueChange = { onEmailChange(it) },
                keyboardType = KeyboardType.Email,
                onNext = { loginFocus.requestFocus(focusDirection = FocusDirection.Up) },
            )
            RegistrationTextInput(
                errorText = data.loginErrorText,
                isError = !data.loginErrorText.isNullOrBlank(),
                labelText = "Логин",
                value = data.login,
                onValueChange = { onLoginChange(it) },
                onNext = { passwordFocus.requestFocus(focusDirection = FocusDirection.Up) },
                focusRequester = loginFocus
            )
            RegistrationTextInput(
                isSecret = true,
                errorText = data.passwordErrorText,
                isError = !data.passwordErrorText.isNullOrBlank(),
                labelText = "Пароль",
                value = data.password,
                onValueChange = { onPasswordChange(it) },
                keyboardType = KeyboardType.Password,
                onNext = { passwordCopyFocus.requestFocus(focusDirection = FocusDirection.Up) },
                focusRequester = passwordFocus
            )
            RegistrationTextInput(
                isSecret = true,
                errorText = data.passwordCopyErrorText,
                isError = !data.passwordCopyErrorText.isNullOrBlank(),
                labelText = "Повторите пароль",
                value = data.passwordCopy,
                onValueChange = { onPasswordCopyChange(it) },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onNext = { focusManager.clearFocus() },
                focusRequester = passwordCopyFocus
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = data.consent, onCheckedChange = { onConsentChange(it) })

                Column {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Согласие на обработку персональных данных.",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Start
                    )
                    AgreementLink(link = "https://example.com")
                }
            }
            Button(
                modifier = Modifier.padding(bottom = 24.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                ),
                onClick = onRegistration,
            ) {
                Text(text = "Зарегистрироваться", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}



@Preview(
    showBackground = true, widthDp = 360
)
@Composable
private fun RegistrationFormPreview() {
    val hazeState = rememberHazeState()
    CompositionLocalProvider(LocalHazeState provides hazeState) {
        val registrationData = RegistrationData(
            email = "PreviewEmail",
            login = "PreviewLogin",
            password = "PreviewPassword",
            passwordCopy = "PreviewPasswordCopy",
            consent = true,
            emailErrorText = null,
            loginErrorText = null,
            passwordErrorText = null,
            passwordCopyErrorText = null,
            consentErrorText = null,
            registrationErrorText = null
        )
        RegistrationForm(
            data = registrationData,
            onEmailChange = {},
            onLoginChange = {},
            onPasswordChange = {},
            onPasswordCopyChange = {},
            onConsentChange = {},
            onRegistration = {})
    }
}
