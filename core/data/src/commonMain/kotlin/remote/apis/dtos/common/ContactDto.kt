package remote.apis.dtos.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ContactDto(
    @SerialName("type") val type: String,
    @SerialName("value") val value: String,
)
