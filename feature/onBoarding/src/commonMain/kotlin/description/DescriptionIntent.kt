package description

internal sealed interface DescriptionIntent {
    object OnRegister : DescriptionIntent
    data class SetRegion(val newValue: String) : DescriptionIntent
    data class SetUniversity(val newValue: String) : DescriptionIntent
    data class SetDepartment(val newValue: String) : DescriptionIntent
    data class SetProgramme(val newValue: String) : DescriptionIntent
    data class SetStudyType(val newValue: String) : DescriptionIntent
    data class SetAbout(val newValue: String) : DescriptionIntent
    data class SetQualities(val newValue: String) : DescriptionIntent
    data class SetSkills(val newValue: String) : DescriptionIntent
    data class SetWorkingHours(val newValue: String) : DescriptionIntent
    data class SetWishes(val newValue: String) : DescriptionIntent
    data class SetWaysToContact(val newValue: String) : DescriptionIntent
}