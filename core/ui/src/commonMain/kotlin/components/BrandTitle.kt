package components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true
)
@Composable
fun BrandTitle(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = "Team",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayMediumEmphasized,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Service",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayMediumEmphasized,
            fontWeight = FontWeight.ExtraBold
        )
    }
}