package features.onBoarding.greeting

sealed interface GreetingIntent {
    object toggleEverLogged : GreetingIntent
}