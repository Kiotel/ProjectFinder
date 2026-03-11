package mapppers

import local.database.enitities.KtorTextEntity
import models.KtorText
import remote.apis.dtos.responses.KtorTextDto

val KtorTextDto.entity get() = KtorTextEntity(text)
val KtorTextDto.domain get() = KtorText(text)

val KtorTextEntity.domain get() = KtorText(text)