package descriptionForm

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val descriptionFormModule = module {
    viewModelOf(::DescriptionFormViewModel)
}
