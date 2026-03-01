package descriptionForm

import OnboardingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun DescriptionFormScreen(
    modifier: Modifier = Modifier, vm: DescriptionFormViewModel, svm: OnboardingViewModel
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
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
            RegistrationForm()
        }
    }
}

@Composable
private fun RegistrationForm(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(12.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Регистрация", style = MaterialTheme.typography.displaySmallEmphasized)
        TextField(
            state = TODO()
        )
    }
}
