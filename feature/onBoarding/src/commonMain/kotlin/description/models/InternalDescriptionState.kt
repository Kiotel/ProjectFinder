package description.models


internal data class InternalDescriptionState(
    // Кто вы?
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",
    val city: String = "",
    val university: String = "",
    val department: String = "",
    val programme: String = "",
    val studyType: String = "",

    // Опыт и Навыки
    val about: String = "",
    val qualities: String = "",
    val interests: String = "",
    val portfolioUrl: String = "",
    val selectedSkills: List<String> = emptyList(),
    val skillSearchQuery: String = "",

    // Связь
    val workingHours: String = "",
    val waysToContact: String = "",
)
