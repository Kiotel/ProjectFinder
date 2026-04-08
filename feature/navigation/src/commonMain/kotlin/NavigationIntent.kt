internal sealed interface NavigationIntent  {
    object CheckIsAuthed: NavigationIntent
    object GetProjects: NavigationIntent
}