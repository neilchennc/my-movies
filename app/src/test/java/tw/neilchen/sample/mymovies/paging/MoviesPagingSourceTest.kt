package tw.neilchen.sample.mymovies.paging

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import tw.neilchen.sample.mymovies.data.Dates
import tw.neilchen.sample.mymovies.data.FakeData
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.data.MovieList
import tw.neilchen.sample.mymovies.network.TmdbApiService

class MoviesPagingSourceTest {

    @MockK
    private lateinit var service: TmdbApiService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun loadPagingData_emptyQuery() = runTest {
        val movieList = MovieList(Dates("", ""), 1, 1, 0, emptyList())
        val expected = PagingSource.LoadResult.Page(emptyList(), null, null)
        val pagingSource = MoviesPagingSource(service, "", "")

        coEvery { service.searchMovies(any(), any(), any()) } returns movieList

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun loadPagingData_emptyList() = runTest {
        val movieList = MovieList(Dates("", ""), 1, 1, 0, emptyList())
        val expected = PagingSource.LoadResult.Page(emptyList(), null, null)
        val pagingSource = MoviesPagingSource(service, "Hello World", "")

        coEvery { service.searchMovies(any(), any(), any()) } returns movieList

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun loadPagingData_nextPage() = runTest {
        val currentPage = 5
        val movies = listOf(FakeData.movie)
        val movieList = MovieList(Dates("", ""), currentPage, 20, movies.size, movies)
        val expected = PagingSource.LoadResult.Page(movies, null, currentPage + 1)
        val pagingSource = MoviesPagingSource(service, "Hello World", "")

        coEvery { service.searchMovies(any(), any(), any()) } returns movieList

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = currentPage,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun loadPagingData_error() = runTest {
        val currentPage = 5
        val exception = HttpException(
            Response.error<Any>(
                500,
                "Internal error".toResponseBody("text/plain".toMediaType())
            )
        )
        val expected = PagingSource.LoadResult.Error<Int, Movie>(exception)
        val pagingSource = MoviesPagingSource(service, "Hello World", "")

        coEvery { service.searchMovies(any(), any(), any()) } throws exception

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = currentPage,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertThat(result).isEqualTo(expected)
    }
}