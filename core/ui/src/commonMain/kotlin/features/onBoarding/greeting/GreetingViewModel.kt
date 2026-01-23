package features.onBoarding.greeting

import androidx.lifecycle.ViewModel
import useCases.GetPlatformUseCase

class GreetingViewModel(
    private val getPlatformUseCase: GetPlatformUseCase
): ViewModel() {
    fun greet(): String {
        return "Hello, ${getPlatformUseCase()}!"
    }

    override fun onCleared() {
        super.onCleared()
        println("GreetingViewModel: cleared")
    }
}