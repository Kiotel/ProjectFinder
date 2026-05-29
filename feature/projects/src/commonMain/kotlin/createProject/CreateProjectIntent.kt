package createProject

internal sealed interface CreateProjectIntent {
    data class SetTitle(val title: String) : CreateProjectIntent
    data class SetDescription(val description: String) : CreateProjectIntent
    data class SetIndustry(val industry: String) : CreateProjectIntent
    data class AddRole(val name: String, val spots: Int) : CreateProjectIntent
    data class RemoveRole(val index: Int) : CreateProjectIntent
    data class AddSkillToRole(val roleIndex: Int, val skill: String) : CreateProjectIntent
    data class RemoveSkillFromRole(val roleIndex: Int, val skill: String) : CreateProjectIntent
    object Submit : CreateProjectIntent
    object ResetSuccess : CreateProjectIntent
}
