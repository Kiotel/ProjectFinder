package utils

internal fun httpErrorMessage(statusCode: Int, context: String): String = when (statusCode) {
    401 -> "Войдите в аккаунт$context"
    403 -> "Нет доступа$context"
    404 -> "Не найдено на сервере$context"
    500 -> "Ошибка сервера$context"
    else -> "Ошибка $statusCode$context"
}

internal fun endpointUnavailableMessage(feature: String): String =
    "$feature пока не реализовано на сервере"
