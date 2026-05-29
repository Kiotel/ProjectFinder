package allProjects

import allProjects.models.ProjectFilter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import models.Project
import useCases.GetProjectsUseCase

internal class ProjectsPagingSource(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val query: String,
    private val filter: ProjectFilter = ProjectFilter.ALL,
    private val currentUserId: String = "",
    private val participationProjectIds: List<Int> = emptyList(),
) : PagingSource<Int, Project>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Project> {
        return try {
            val currentPageNumber = params.key ?: 1

            val response = getProjectsUseCase(
                page = currentPageNumber,
                limit = params.loadSize,
            ).first()

            val pageItems = response.fold(
                onSuccess = { projects ->
                    projects.filter { project ->
                        val matchesQuery = query.isBlank() || 
                            project.title.contains(query, ignoreCase = true) || 
                            project.description.contains(query, ignoreCase = true) ||
                            project.authorName.contains(query, ignoreCase = true)
                        
                        val matchesFilter = when (filter) {
                            ProjectFilter.ALL -> true
                            ProjectFilter.MINE -> project.authorId == currentUserId
                            ProjectFilter.JOINED -> project.id.toIntOrNull() in participationProjectIds
                        }
                        
                        matchesQuery && matchesFilter
                    }
                },
                onFailure = { throw it },
            )

            LoadResult.Page(
                data = pageItems,
                prevKey = if (currentPageNumber == 1) null else currentPageNumber - 1,
                nextKey = if (pageItems.size < params.loadSize) null else currentPageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Project>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
