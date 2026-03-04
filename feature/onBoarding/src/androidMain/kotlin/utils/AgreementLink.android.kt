package utils

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration

@Composable
actual fun AgreementLink(link: String) {
    val context = LocalContext.current
    Text(
        text = "Соглашение",
        style = MaterialTheme.typography.labelSmall.copy(
            color = Color.White,
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier.clickable {
            println("Trying to open2: $link")
            openUrl(context, link)
        }
    )
}