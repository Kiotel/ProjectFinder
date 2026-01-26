import androidx.lifecycle.ViewModel

class OnboardingViewModel(
) : ViewModel() {
    fun getText(): String {
        return "OnBoardingViewModel with id: $this"
    }

    override fun onCleared() {
        super.onCleared()
        println("OnBoardingViewModel: cleared")
    }
}