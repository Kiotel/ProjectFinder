package descriptionForm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DescriptionFormViewModel(
) : ViewModel() {
    private val _text = MutableStateFlow("")

    fun setData(text: String){
        _text.value = text
    }

    fun getText(): String{
        return _text.value
    }

    override fun onCleared() {
        super.onCleared()
        println("DescriptionFormViewModel: cleared")
    }
}