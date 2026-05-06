package utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

data class SnackbarMessage(
    val id: Long,
    val message: String,
    val duration: SnackbarDuration
)

@Stable
class SnackBarManager {
    private val _currentMessage = MutableStateFlow<SnackbarMessage?>(null)
    val currentMessage: StateFlow<SnackbarMessage?> = _currentMessage.asStateFlow()

    private var messageIdCounter: Long = 0

    fun showMessage(message: StringResource, duration: SnackbarDuration = SnackbarDuration.Short) {
        CoroutineScope(Dispatchers.Main).launch {
            messageIdCounter++
            val text: String = getString(message)
            _currentMessage.value = SnackbarMessage(
                id = messageIdCounter,
                message = text,
                duration = duration
            )
        }
    }

    fun showMessage(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        messageIdCounter++
        _currentMessage.value = SnackbarMessage(
            id = messageIdCounter,
            message = message,
            duration = duration
        )
    }

    fun messageShown(messageId: Long) {
        _currentMessage.update { current ->
            if (current?.id == messageId) null else current
        }
    }
}