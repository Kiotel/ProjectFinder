package allProjects

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import models.Project
import useCases.GetProjectsUseCase

internal class ProjectsPagingSource(
    private val getProjectsUseCase: GetProjectsUseCase,
    val query: String
) : PagingSource<Int, Project>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Project> {
        return try {
            // Start at page 1 if params.key is null
            val currentPageNumber = params.key ?: 1

            // Use params.loadSize to respect the pageSize configured in the ViewModel's Pager
            val response = getProjectsUseCase(
                page = currentPageNumber,
                limit = params.loadSize
            ).first()

            val result = response.fold(
                onSuccess = { it },
                onFailure = { throw it } // Throw error to trigger LoadResult.Error
            )

            LoadResult.Page(
                data = result,
                // If we are on page 1, there is no previous page
                prevKey = if (currentPageNumber == 1) null else currentPageNumber - 1,
                // If the API returned an empty list (or less than requested), we reached the end
                nextKey = if (result.isEmpty()) null else currentPageNumber + 1
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