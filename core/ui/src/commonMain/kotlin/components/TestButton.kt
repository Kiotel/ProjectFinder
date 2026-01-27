package components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TestButton(
    modifier: Modifier = Modifier, text: String, onClick: () -> Unit
) {
    Button(modifier = modifier, onClick = onClick) {
        Text(text = text)
    }
}
