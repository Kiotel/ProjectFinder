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
            val currentPageNumber = params.key ?: 1

            val response = getProjectsUseCase(
                page = currentPageNumber,
                limit = params.loadSize
            ).first()

            val result = response.fold(
                onSuccess = { it },
                onFailure = { throw it }
            )

            LoadResult.Page(
                data = result,
                prevKey = if (currentPageNumber == 1) null else currentPageNumber - 1,
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