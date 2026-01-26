package features.onBoarding.descriptionForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import features.onBoarding.OnboardingViewModel

@Composable
fun DescriptionFormScreen(
    modifier: Modifier = Modifier, vm: DescriptionFormViewModel, svm: OnboardingViewModel
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text("Text in local viewModel is: ${vm.getText()}")
            Text("Text in shared viewModel is: ${svm.getText()}")
        }
    }
}