package registrationForm

import OnboardingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import registrationForm.models.RegistrationFormData

@Composable
internal fun RegistrationFormScreen(
    modifier: Modifier = Modifier, vm: RegistrationFormViewModel, svm: OnboardingViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(), containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Добро пожаловать!",
                style = MaterialTheme.typography.displaySmallEmphasized,
                textAlign = TextAlign.Center
            )
            RegistrationForm(
                data = RegistrationFormData(
                    email = uiState.email,
                    login = uiState.login,
                    password = uiState.password,
                    passwordCopy = uiState.passwordCopy,
                    consent = uiState.consent
                ),
                onEmailChange = { vm.handleIntent(RegistrationIntent.SetEmail(it)) },
                onLoginChange = { vm.handleIntent(RegistrationIntent.SetLogin(it)) },
                onPasswordChange = { vm.handleIntent(RegistrationIntent.SetPassword(it)) },
                onPasswordCopyChange = { vm.handleIntent(RegistrationIntent.SetPasswordCopy(it)) },
                onConsentChange = { vm.handleIntent(RegistrationIntent.SetConsent(it)) })
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
) {
    Column(
        modifier = modifier.fillMaxSize().padding(12.dp).background(
            color = MaterialTheme.colorScheme.surfaceContainer, shape = MaterialTheme.shapes.large
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Регистрация",
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.displaySmallEmphasized
        )
        RegistrationTextInput(
            labelText = "Почта", value = data.email, onValueChange = { onEmailChange(it) })
        RegistrationTextInput(
            labelText = "Логин", value = data.login, onValueChange = { onLoginChange(it) })
        RegistrationTextInput(
            labelText = "Пароль", value = data.password, onValueChange = { onPasswordChange(it) })
        RegistrationTextInput(
            labelText
            = "Повторите пароль",
            value = data.passwordCopy,
            onValueChange = { onPasswordCopyChange(it) })
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = data.consent, onCheckedChange = { onConsentChange(it) })
            Text(
                text = "Я согласен на обработку персональных данных",
            )
        }
    }


}

@Composable
private fun RegistrationTextInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (newValue: String) -> Unit,
    supportingText: String? = null,
    placeHolderText: String? = null,
    labelText: String? = null,
    isError: Boolean = false,
) {
    OutlinedTextField(
        supportingText = {
            if (supportingText != null) {
                Text(
                    text = supportingText, style = MaterialTheme.typography.labelMedium
                )
            }
        },
        placeholder = {
            if (placeHolderText != null) {
                Text(
                    text = placeHolderText, style = MaterialTheme.typography.labelMedium
                )
            }
        },
        label = {
            if (labelText != null) {
                Text(
                    text = labelText, style = MaterialTheme.typography.labelMedium
                )
            }
        },
        isError = isError,
        modifier = modifier,
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