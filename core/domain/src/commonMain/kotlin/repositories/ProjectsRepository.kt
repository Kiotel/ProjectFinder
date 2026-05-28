package repositories

import kotlinx.coroutines.flow.Flow
import models.Project
import models.ProjectApplicant
import models.ProjectComment
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
        roles: List<Pair<String, Int>>,
    ): Result<Project>
}
