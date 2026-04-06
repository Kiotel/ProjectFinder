package local.secureStore

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import kotlinx.serialization.Serializable

@Serializable
private data class AuthTokens(
    val accessToken: String = "",
    val refreshToken: String = ""
)

class TokenStore(
    private val kSafe: KSafe
) {
    private var tokens by kSafe(AuthTokens())

    var accessToken: String
        get() = tokens.accessToken
        set(newAccessToken) {
            tokens = AuthTokens(
                accessToken = newAccessToken,
                refreshToken = tokens.refreshToken
            )
        }

    var refreshToken: String
        get() = tokens.refreshToken
        set(newRefreshToken) {
            tokens = AuthTokens(
                accessToken = tokens.accessToken,
                refreshToken = newRefreshToken
            )
        }

    fun setTokens(accessToken: String, refreshToken: String) {
        tokens = AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}