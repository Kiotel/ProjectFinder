package repositories

import kotlinx.coroutines.flow.Flow
import models.Project
import models.ProjectApplicant
import models.ProjectComment
import models.ProjectMember
import models.ProjectRole
import kotlin.time.Duration

interface ProjectsRepository {
    fun getProjects(page: Int, limit: Int, ttl: Duration): Flow<Result<List<Project>>>
    fun likeProject(projectId: String): Flow<Result<Boolean>>
    fun getComments(projectId: String): Flow<Result<List<ProjectComment>>>
    suspend fun postComment(projectId: String, content: String): Result<Unit>
    suspend fun postResponse(projectId: String, message: String): Result<Unit>
    suspend fun getApplicants(projectId: String): Result<List<ProjectApplicant>>
    suspend fun updateApplicantStatus(responseId: String, status: String): Result<Unit>
    suspend fun createProject(
        title: String,
        description: String?,
        industry: String?,
        roles: List<ProjectRole>,
    ): Result<Project>
    suspend fun updateProject(
        projectId: String,
        title: String,
        description: String?,
        industry: String?,
        roles: List<ProjectRole>,
    ): Result<Unit>
    suspend fun deleteProject(projectId: String): Result<Unit>
    suspend fun getMyParticipationProjects(): Result<List<Int>>
    suspend fun getProjectMembers(projectId: String): Result<List<ProjectMember>>
}
