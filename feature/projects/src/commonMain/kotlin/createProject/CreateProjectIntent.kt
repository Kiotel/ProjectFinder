package createProject

internal sealed interface CreateProjectIntent {
    data class SetTitle(val title: String) : CreateProjectIntent
    data class SetDescription(val description: String) : CreateProjectIntent
    data class SetIndustry(val industry: String) : CreateProjectIntent
    data class AddRole(val name: String, val spots: Int) : CreateProjectIntent
    data class RemoveRole(val index: Int) : CreateProjectIntent
    object Submit : CreateProjectIntent
    object ResetSuccess : CreateProjectIntent
}
