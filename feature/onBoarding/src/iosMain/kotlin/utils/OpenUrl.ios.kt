package utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrl(context: Any?, url: String) {
    NSURL.URLWithString(url)?.let { nsUrl ->
        if (UIApplication.sharedApplication.canOpenURL(nsUrl)) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }
}