package greeting

internal sealed interface GreetingIntent {
    object toggleEverLogged : GreetingIntent
}