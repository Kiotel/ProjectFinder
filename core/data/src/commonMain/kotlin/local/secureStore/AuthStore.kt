package local.secureStore

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import kotlinx.serialization.Serializable

@Serializable
private data class AuthData(
    val userId: String = "",
    val accessToken: String = "",
    val refreshToken: String = ""
)

internal class AuthStore(
    kSafe: KSafe
) {
    private var authData by kSafe(AuthData())

    var accessToken: String
        get() = authData.accessToken
        set(newAccessToken) {
            authData = AuthData(
                userId = authData.userId,
                accessToken = newAccessToken,
                refreshToken = authData.refreshToken
            )
        }

    var userId: String
        get() = authData.userId
        set(newUserId) {
            authData = AuthData(
                userId = newUserId,
                accessToken = authData.accessToken,
                refreshToken = authData.refreshToken
            )
        }
    var refreshToken: String
        get() = authData.refreshToken
        set(newRefreshToken) {
            authData = AuthData(
                userId = authData.userId,
                accessToken = authData.accessToken,
                refreshToken = newRefreshToken
            )
        }

    fun setAuthData(userId: String, accessToken: String, refreshToken: String) {
        authData = AuthData(
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}