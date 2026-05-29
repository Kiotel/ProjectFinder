package local.secureStore

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke

class ProfileFillStore(
    kSafe: KSafe
) {
    private var isFilled by kSafe(false)

    fun markAsFilled() {
        isFilled = true
    }

    fun isProfileFilled(): Boolean = isFilled

    fun clear() {
        isFilled = false
    }
}
