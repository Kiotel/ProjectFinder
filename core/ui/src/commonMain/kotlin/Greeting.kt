import useCases.GetPlatformUseCase

class Greeting(
    private val getPlatformUseCase: GetPlatformUseCase
) {
    fun greet(): String {
        return "Hello, ${getPlatformUseCase()}!"
    }
}