package description.models

internal data class DescriptionState(
    val region: String,
    val university: String,
    val department: String,
    val programme: String,
    val studyType: String,
    val about: String,
    val qualities: String,
    val skills: String,
    val workingHours: String,
    val wishes: String,
    val waysToContact: String,
) {
    internal constructor(internalDescriptionState: InternalDescriptionState) : this(
        region = internalDescriptionState.region,
        university = internalDescriptionState.university,
        department = internalDescriptionState.department,
        programme = internalDescriptionState.programme,
        studyType = internalDescriptionState.studyType,
        about = internalDescriptionState.about,
        qualities = internalDescriptionState.qualities,
        skills = internalDescriptionState.skills,
        workingHours = internalDescriptionState.workingHours,
        wishes = internalDescriptionState.wishes,
        waysToContact = internalDescriptionState.waysToContact
    )
}
