package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.rememberHazeState
import modifiers.cheapGlassEffect
import utils.LocalHazeState

@Composable
fun RegistrationTextInput(
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
    focusRequester: FocusRequester = FocusRequester.Default,
    isSecret: Boolean = false
) {
    var hide by remember { mutableStateOf(true) }
    val visualTransformation =
        if (hide && isSecret) PasswordVisualTransformation() else VisualTransformation.None

    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (labelText != null) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.labelLargeEmphasized,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().cheapGlassEffect(
                shape = MaterialTheme.shapes.medium,
                fillAlpha = 0.2f
            )
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType, imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(onNext = { onNext() }, onDone = { onNext() }, onSend = {onNext()}, onSearch = {onNext()}, onGo = {onNext()}),
                placeholder = {
                    if (placeHolderText != null) {
                        Text(text = placeHolderText, style = MaterialTheme.typography.bodyMedium)
                    }
                },
                trailingIcon = {
                    if (isSecret) {
                        IconButton(onClick = { hide = !hide }) {
                            Icon(
                                if (hide) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
        }

        Text(
            text = if (isError) errorText ?: "" else "",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun RegistrationTextInputPreview() {
    val hazeState = rememberHazeState()
    CompositionLocalProvider(LocalHazeState provides hazeState) {
        RegistrationTextInput(
            value = "Preview", onValueChange = {})
    }
}