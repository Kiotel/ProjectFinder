package utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

data class SnackbarMessage(
    val id: Long,
    val messageResource: StringResource,
    val duration: SnackbarDuration
)

@Stable
class SnackBarManager {
    private val _currentMessage = MutableStateFlow<SnackbarMessage?>(null)
    val currentMessage: StateFlow<SnackbarMessage?> = _currentMessage.asStateFlow()

    private var messageIdCounter: Long = 0

    fun showMessage(message: StringResource, duration: SnackbarDuration = SnackbarDuration.Short) {
        messageIdCounter++

        _currentMessage.value = SnackbarMessage(
            id = messageIdCounter,
            messageResource = message,
            duration = duration
        )
    }

    fun messageShown(messageId: Long) {
        _currentMessage.update { current ->
            if (current?.id == messageId) null else current
        }
    }
}