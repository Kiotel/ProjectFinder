internal sealed interface NavigationIntent  {
    object CheckIsAuthed: NavigationIntent
    object GetProjects: NavigationIntent
    object Logout: NavigationIntent
    object DeleteAccount: NavigationIntent
}
