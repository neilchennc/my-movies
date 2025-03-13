package tw.neilchen.sample.mymovies.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.network.TmdbApiService
import java.io.IOException

class MoviesPagingSource(
    private val service: TmdbApiService,
    private val query: String,
    private val language: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Movie> {
        try {
            if (query.isEmpty()) {
                // Return empty result if empty query string
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                // Start refresh at page 1 if undefined.
                val nextPageNumber = params.key ?: 1
                val response = service.searchMovies(
                    query = query,
                    page = nextPageNumber,
                    language = language
                )
                return LoadResult.Page(
                    data = response.results,
                    prevKey = null, // Only paging forward.
                    nextKey = if (response.results.isEmpty()) null else nextPageNumber.inc()
                )
            }
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}