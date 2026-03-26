package descriptionForm

import OnboardingViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun DescriptionFormScreen(
    modifier: Modifier = Modifier, vm: DescriptionFormViewModel, svm: OnboardingViewModel
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent
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
            Registration()
        }
    }
}

@Composable
private fun Registration(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Регистрация", style = MaterialTheme.typography.displaySmallEmphasized)
    }
}
