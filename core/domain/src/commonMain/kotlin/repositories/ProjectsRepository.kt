package repositories

import kotlinx.coroutines.flow.Flow
import models.Project
import kotlin.time.Duration

interface ProjectsRepository {
    fun getProjects(page: Int, limit: Int, ttl: Duration): Flow<Result<List<Project>>>

    fun likeProject(projectId: String): Flow<Result<Boolean>>

    fun getProject(projectId: String): Flow<Project>
}