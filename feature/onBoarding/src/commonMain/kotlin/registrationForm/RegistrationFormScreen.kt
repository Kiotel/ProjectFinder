package registrationForm

import OnboardingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import registrationForm.models.RegistrationFormData
import utils.AgreementLink

@Composable
internal fun RegistrationFormScreen(
    modifier: Modifier = Modifier, vm: RegistrationFormViewModel, svm: OnboardingViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier.imePadding().verticalScroll(scrollState).fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding() + 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            Text(
                text = "Добро пожаловать!",
                style = MaterialTheme.typography.displaySmallEmphasized,
                textAlign = TextAlign.Center
            )
            RegistrationForm(
                data = RegistrationFormData(
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
                    registrationErrorText = uiState.registrationErrorText?.let { stringResource(it) }),
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
    data: RegistrationFormData,
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

    Column(
        modifier = modifier.fillMaxWidth().padding(12.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Регистрация",
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.displaySmallEmphasized
        )
        RegistrationTextInput(
            errorText = data.emailErrorText,
            isError = !data.emailErrorText.isNullOrBlank(),
            labelText = "Почта",
            value = data.email,
            onValueChange = { onEmailChange(it) },
            keyboardType = KeyboardType.Email,
            onNext = { loginFocus.requestFocus() },
        )
        RegistrationTextInput(
            errorText = data.loginErrorText,
            isError = !data.loginErrorText.isNullOrBlank(),
            labelText = "Логин",
            value = data.login,
            onValueChange = { onLoginChange(it) },
            onNext = { passwordFocus.requestFocus() },
            focusRequester = loginFocus
        )
        RegistrationTextInput(
            errorText = data.passwordErrorText,
            isError = !data.passwordErrorText.isNullOrBlank(),
            labelText = "Пароль",
            value = data.password,
            onValueChange = { onPasswordChange(it) },
            keyboardType = KeyboardType.Password,
            onNext = { passwordCopyFocus.requestFocus() },
            focusRequester = passwordFocus
        )
        RegistrationTextInput(
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
                    text = "Я согласен на обработку персональных данных.",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
                AgreementLink(link = "https://example.com")
            }

        }
        FilledTonalButton(
            modifier = Modifier.padding(bottom = 24.dp),
            onClick = onRegistration,
        ) {
            Text(text = "Зарегистрироваться", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun RegistrationTextInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (newValue: String) -> Unit,
    errorText: String? = null,
    placeHolderText: String? = null,
    labelText: String? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester.Default
) {
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() },
            onDone = { onNext() }
        ),
        supportingText = {
            Text(
                text = if (isError) errorText ?: "" else " ",
                style = MaterialTheme.typography.labelMedium
            )
        },
        placeholder = {
            Text(
                text = placeHolderText ?: "", style = MaterialTheme.typography.labelMedium
            )
        },
        label = {
            if (labelText != null) {
                Text(
                    text = labelText, style = MaterialTheme.typography.labelMedium
                )
            }
        },
        isError = isError,
        modifier = modifier.focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun RegistrationTextInputPreview() {
    RegistrationTextInput(
        value = "Preview", onValueChange = {})
}

@Preview(
    showBackground = true, widthDp = 360
)
@Composable
private fun RegistrationFormPreview() {
    val registrationData = RegistrationFormData(
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