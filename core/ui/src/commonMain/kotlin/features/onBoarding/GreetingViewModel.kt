package features.onBoarding

import androidx.lifecycle.ViewModel
import useCases.GetPlatformUseCase

class GreetingViewModel(
    private val getPlatformUseCase: GetPlatformUseCase
): ViewModel() {
    fun greet(): String {
        return "Hello, ${getPlatformUseCase()}!"
    }
}