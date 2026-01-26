package apis.dtos

import models.KtorText
import kotlin.jvm.JvmInline

@JvmInline
value class KtorTextDto(
    val text: String
) {
    fun toDomain(): KtorText = KtorText(text)
}