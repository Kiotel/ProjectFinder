import kotlin.concurrent.Volatile

/**
 * In-memory bridge между ProfileFillStore (core:data) и NavigationViewModel (feature:navigation).
 * Хранит флаг isProfileFilled, который синхронизируется из KSafe-персистентного хранилища
 * при старте приложения (через dataModule).
 *
 * feature:navigation НЕ может напрямую зависеть от core:data, поэтому флаг пробрасывается
 * через domain-слой.
 */
class ProfileFillManager {
    @Volatile
    var isFilled: Boolean = false
        private set

    fun markAsFilled() {
        isFilled = true
    }

    fun restore(filled: Boolean) {
        isFilled = filled
    }

    fun clear() {
        isFilled = false
    }
}
