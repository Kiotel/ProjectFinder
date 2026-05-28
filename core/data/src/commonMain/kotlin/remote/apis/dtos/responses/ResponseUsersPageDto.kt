package remote.apis.dtos.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseUsersPageDto(
    @SerialName("data") val data: List<ResponseUserDto> = emptyList(),
    @SerialName("pagination") val pagination: PaginationDto? = null,
)

@Serializable
internal data class PaginationDto(
    @SerialName("currentPage") val currentPage: Int? = null,
    @SerialName("pageSize") val pageSize: Int? = null,
    @SerialName("totalItems") val totalItems: Int? = null,
    @SerialName("totalPages") val totalPages: Int? = null,
)
