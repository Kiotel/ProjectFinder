import detailedProfile.detailedProfileModule
import notifications.NotificationsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import participantProfile.ParticipantProfileViewModel
import usersSearch.UsersSearchViewModel

val profileModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::UsersSearchViewModel)
    viewModelOf(::NotificationsViewModel)
    viewModelOf(::ParticipantProfileViewModel)

    includes(detailedProfileModule)
}
