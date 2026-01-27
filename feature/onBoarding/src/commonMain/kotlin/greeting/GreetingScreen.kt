package greeting

import GreetingViewModel
import OnBoardingIntent
import OnboardingViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.TestButton

@Composable
internal fun GreetingScreen(
    modifier: Modifier = Modifier,
    vm: GreetingViewModel, svm: OnboardingViewModel, goToDescriptionForm: () -> Unit
) {
    val sharedState by svm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Team service", style = MaterialTheme.typography.displayMediumEmphasized)
            TestButton(onClick = {
                svm.handleIntent(OnBoardingIntent.SetToken)
            }, text = sharedState.token.toString())
            TestButton(onClick = { goToDescriptionForm() }, text = "Продолжить")
        }
    }
}
