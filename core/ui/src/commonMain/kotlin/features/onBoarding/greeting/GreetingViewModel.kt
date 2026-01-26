package features.onBoarding.greeting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import useCases.GetKtorTextUseCase
import useCases.GetPlatformUseCase
import utils.Resource

class GreetingViewModel(
    private val getPlatformUseCase: GetPlatformUseCase,
    private val getKtorText: GetKtorTextUseCase,
) : ViewModel() {
    private var _ktorText = MutableStateFlow("Loading...")
    val ktorText = _ktorText.asStateFlow()

    init {
        fetchText()
    }

    private fun fetchText() {
        viewModelScope.launch {
            getKtorText().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        if (result.data != null) {
                            _ktorText.value = result.data!!.text
                        }
                    }
                    is Resource.Success -> {
                        _ktorText.value = result.data?.text ?: "Empty"
                    }
                    is Resource.Error -> {
                        _ktorText.value = "Error"
                    }
                }
            }
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