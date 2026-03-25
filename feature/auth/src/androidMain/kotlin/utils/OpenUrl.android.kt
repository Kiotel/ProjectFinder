package utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

actual fun openUrl(context: Any?, url: String) {
    println("Trying to open $url")
    val context = context as? Context ?: return

    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}