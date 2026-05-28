package local.secureStore

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import kotlinx.serialization.Serializable

@Serializable
private data class FormData(
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",
    val city: String = "",
    val university: String = "",
    val department: String = "",
    val programme: String = "",
    val studyType: String = "",
    val about: String = "",
    val qualities: String = "",
    val interests: String = "",
    val portfolioUrl: String = "",
    val selectedSkills: List<String> = emptyList(),
    val workingHours: String = "",
    val waysToContact: String = "",
)

class FormDataStore(
    kSafe: KSafe
) {
    private var formData by kSafe(FormData())

    var firstName: String
        get() = formData.firstName
        set(value) { formData = formData.copy(firstName = value) }

    var lastName: String
        get() = formData.lastName
        set(value) { formData = formData.copy(lastName = value) }

    var age: String
        get() = formData.age
        set(value) { formData = formData.copy(age = value) }

    var city: String
        get() = formData.city
        set(value) { formData = formData.copy(city = value) }

    var university: String
        get() = formData.university
        set(value) { formData = formData.copy(university = value) }

    var department: String
        get() = formData.department
        set(value) { formData = formData.copy(department = value) }

    var programme: String
        get() = formData.programme
        set(value) { formData = formData.copy(programme = value) }

    var studyType: String
        get() = formData.studyType
        set(value) { formData = formData.copy(studyType = value) }

    var about: String
        get() = formData.about
        set(value) { formData = formData.copy(about = value) }

    var qualities: String
        get() = formData.qualities
        set(value) { formData = formData.copy(qualities = value) }

    var interests: String
        get() = formData.interests
        set(value) { formData = formData.copy(interests = value) }

    var portfolioUrl: String
        get() = formData.portfolioUrl
        set(value) { formData = formData.copy(portfolioUrl = value) }

    var selectedSkills: List<String>
        get() = formData.selectedSkills
        set(value) { formData = formData.copy(selectedSkills = value) }

    var workingHours: String
        get() = formData.workingHours
        set(value) { formData = formData.copy(workingHours = value) }

    var waysToContact: String
        get() = formData.waysToContact
        set(value) { formData = formData.copy(waysToContact = value) }

    fun clear() {
        formData = FormData()
    }
}