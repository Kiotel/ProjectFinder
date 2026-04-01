package description.models


internal data class InternalDescriptionState(
    // Учебное заведение
    val region: String = "",
    val university: String = "",
    val department: String = "",
    val programme: String = "",
    val studyType: String = "",

    // Личная информация
    val about: String = "",
    val qualities: String = "",
    val skills: String = "",

    // Контакты
    val workingHours: String = "",
    val wishes: String = "",
    val waysToContact: String = "",
)
