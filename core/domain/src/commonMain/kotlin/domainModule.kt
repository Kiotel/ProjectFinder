import org.koin.dsl.module
import useCases.BookmarkUserUseCase
import useCases.CreateProjectUseCase
import useCases.DeleteAccountUseCase
import useCases.DeleteProjectUseCase
import useCases.GetApplicantsUseCase
import useCases.GetCommentsUseCase
import useCases.GetIsAuthedUseCase
import useCases.GetMyParticipationProjectsUseCase
import useCases.GetNotificationsUseCase
import useCases.GetProjectMembersUseCase
import useCases.GetProjectsUseCase
import useCases.GetUserInfoUseCase
import useCases.GetUserProfileUseCase
import useCases.LikeProjectUseCase
import useCases.LoginUseCase
import useCases.LogoutUseCase
import useCases.PostCommentUseCase
import useCases.PostProjectResponseUseCase
import useCases.RegisterUseCase
import useCases.SearchUsersUseCase
import useCases.UpdateApplicantStatusUseCase
import useCases.UpdateProjectUseCase
import useCases.UpdateUserProfileUseCase

val domainModule = module {
    single { ProfileFillManager() }

    factory<GetIsAuthedUseCase> { GetIsAuthedUseCase(get()) }
    factory<RegisterUseCase> { RegisterUseCase(get()) }
    factory<LoginUseCase> { LoginUseCase(get()) }
    factory<GetUserInfoUseCase> { GetUserInfoUseCase(get()) }
    factory<GetUserProfileUseCase> { GetUserProfileUseCase(get()) }
    factory<LikeProjectUseCase> { LikeProjectUseCase(get()) }
    factory<GetProjectsUseCase> { GetProjectsUseCase(get()) }
    factory<SearchUsersUseCase> { SearchUsersUseCase(get()) }
    factory<UpdateUserProfileUseCase> { UpdateUserProfileUseCase(get()) }
    factory<BookmarkUserUseCase> { BookmarkUserUseCase(get()) }
    factory<GetNotificationsUseCase> { GetNotificationsUseCase(get()) }
    factory<GetCommentsUseCase> { GetCommentsUseCase(get()) }
    factory<PostCommentUseCase> { PostCommentUseCase(get()) }
    factory<PostProjectResponseUseCase> { PostProjectResponseUseCase(get()) }
    factory<CreateProjectUseCase> { CreateProjectUseCase(get()) }
    factory<UpdateProjectUseCase> { UpdateProjectUseCase(get()) }
    factory<DeleteProjectUseCase> { DeleteProjectUseCase(get()) }
    factory<LogoutUseCase> { LogoutUseCase(get()) }
    factory<DeleteAccountUseCase> { DeleteAccountUseCase(get()) }
    factory<GetApplicantsUseCase> { GetApplicantsUseCase(get()) }
    factory<UpdateApplicantStatusUseCase> { UpdateApplicantStatusUseCase(get()) }
    factory<GetMyParticipationProjectsUseCase> { GetMyParticipationProjectsUseCase(get()) }
    factory<GetProjectMembersUseCase> { GetProjectMembersUseCase(get()) }
}
