package features.onBoarding.greeting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import useCases.GetKtorTextUseCase
import useCases.GetPlatformUseCase

class GreetingViewModel(
    private val getPlatformUseCase: GetPlatformUseCase,
    private val getKtorText: GetKtorTextUseCase,
) : ViewModel() {
    private var _ktorText = MutableStateFlow("Loading...")
    val ktorText = _ktorText.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getKtorText().fold(
                onSuccess = {
                    _ktorText.value = it
                },
                onFailure = {
                    _ktorText.value = "Error during fetching"
                }
            )
        }
    }

    fun greet(): String {
        return "Hello, ${getPlatformUseCase()}!"
    }

    override fun onCleared() {
        super.onCleared()
        println("GreetingViewModel: cleared")
    }
}