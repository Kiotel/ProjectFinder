package mapppers

import remote.apis.dtos.KtorTextDto
import local.database.enitities.KtorTextEntity
import models.KtorText

val KtorTextDto.entity get() = KtorTextEntity(text)
val KtorTextDto.domain get() = KtorText(text)

val KtorTextEntity.domain get() = KtorText(text)