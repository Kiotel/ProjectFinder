package utils

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration

@Composable
actual fun AgreementLink(link: String) {
    Text(
        text = "Соглашение",
        style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier.clickable {
            openUrl(null, link)
        }
    )
}