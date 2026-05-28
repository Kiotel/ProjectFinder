package description.models

internal data class DescriptionState(
    val firstName: String,
    val lastName: String,
    val age: String,
    val city: String,
    val university: String,
    val department: String,
    val programme: String,
    val studyType: String,
    val about: String,
    val qualities: String,
    val interests: String,
    val portfolioUrl: String,
    val selectedSkills: List<String>,
    val skillSearchQuery: String,
    val availableSkills: List<String>,
    val workingHours: String,
    val waysToContact: String,
) {
    internal constructor(
        internalDescriptionState: InternalDescriptionState,
        availableSkills: List<String>
    ) : this(
        firstName = internalDescriptionState.firstName,
        lastName = internalDescriptionState.lastName,
        age = internalDescriptionState.age,
        city = internalDescriptionState.city,
        university = internalDescriptionState.university,
        department = internalDescriptionState.department,
        programme = internalDescriptionState.programme,
        studyType = internalDescriptionState.studyType,
        about = internalDescriptionState.about,
        qualities = internalDescriptionState.qualities,
        interests = internalDescriptionState.interests,
        portfolioUrl = internalDescriptionState.portfolioUrl,
        selectedSkills = internalDescriptionState.selectedSkills,
        skillSearchQuery = internalDescriptionState.skillSearchQuery,
        availableSkills = availableSkills,
        workingHours = internalDescriptionState.workingHours,
        waysToContact = internalDescriptionState.waysToContact
    )
}
