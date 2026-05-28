package description

internal sealed interface DescriptionIntent {
    object OnRegister : DescriptionIntent
    
    // Кто вы?
    data class SetFirstName(val newValue: String) : DescriptionIntent
    data class SetLastName(val newValue: String) : DescriptionIntent
    data class SetAge(val newValue: String) : DescriptionIntent
    data class SetCity(val newValue: String) : DescriptionIntent
    data class SetUniversity(val newValue: String) : DescriptionIntent
    data class SetDepartment(val newValue: String) : DescriptionIntent
    data class SetProgramme(val newValue: String) : DescriptionIntent
    data class SetStudyType(val newValue: String) : DescriptionIntent

    // Опыт и Навыки
    data class SetAbout(val newValue: String) : DescriptionIntent
    data class SetQualities(val newValue: String) : DescriptionIntent
    data class SetInterests(val newValue: String) : DescriptionIntent
    data class SetPortfolio(val newValue: String) : DescriptionIntent
    data class AddSkill(val skill: String) : DescriptionIntent
    data class RemoveSkill(val skill: String) : DescriptionIntent
    data class SetSkillSearch(val query: String) : DescriptionIntent
    
    // Связь
    data class SetWorkingHours(val newValue: String) : DescriptionIntent
    data class SetWaysToContact(val newValue: String) : DescriptionIntent
}
