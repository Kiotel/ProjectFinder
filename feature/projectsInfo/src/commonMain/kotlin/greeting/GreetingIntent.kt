package greeting

internal sealed interface GreetingIntent {
    object ToggleEverLogged : GreetingIntent
}