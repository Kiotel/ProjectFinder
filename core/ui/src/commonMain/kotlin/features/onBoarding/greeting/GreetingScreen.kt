package features.onBoarding.greeting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import features.onBoarding.OnboardingViewModel
import org.jetbrains.compose.resources.painterResource
import projectfinder.core.ui.generated.resources.Res
import projectfinder.core.ui.generated.resources.compose_multiplatform

@Composable
fun GreetingScreen(
    modifier: Modifier = Modifier,
    vm: GreetingViewModel, svm: OnboardingViewModel, goToDescription: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    val ktorText by vm.ktorText.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { vm.greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.compose_multiplatform),
                        contentDescription = null
                    )
                    Text("Compose: $greeting")
                    Text(svm.getText())
                    Button(
                        onClick = goToDescription, content = {
                            Text("Go to description")
                        })
                    Text("Ktor text: $ktorText")
                }
            }
        }
    }
}