package tw.neilchen.sample.mymovies.repository

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import tw.neilchen.sample.mymovies.data.Dates
import tw.neilchen.sample.mymovies.data.MovieList
import tw.neilchen.sample.mymovies.network.TmdbApiService

@OptIn(ExperimentalCoroutinesApi::class)
class TmdbMoviesRepositoryTest {

    private val dispatcher = StandardTestDispatcher()
    private val apiService = mockk<TmdbApiService>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getNowPlayingMovies_success() = runTest {
        val fakeMovieList = MovieList(Dates("", ""), 1, 1, 0, emptyList())

        coEvery { apiService.getNowPlayingMovies(0, "") } returns fakeMovieList

        val tmdb = TmdbMoviesRepository(apiService)

        // Original
        val result = tmdb.getNowPlayingMovies(0, "").first()
        assertEquals(fakeMovieList, result)

        // Turbine
        tmdb.getNowPlayingMovies(0, "").test {
            assertEquals(fakeMovieList, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun getNowPlayingMovies_failed() = runTest {
        coEvery { apiService.getNowPlayingMovies(0, "") } throws Exception("Failed")

        val tmdb = TmdbMoviesRepository(apiService)

        tmdb.getNowPlayingMovies(0, "").test {
            assertEquals("Failed", awaitError().message)
        }
    }

    @Test
    fun getNowPlayingMovies_serverError() = runTest {
        coEvery { apiService.getNowPlayingMovies(0, "") } throws
                HttpException(
                    Response.error<Any>(
                        500,
                        "Internal error".toResponseBody("text/plain".toMediaType())
                    )
                )

        val tmdb = TmdbMoviesRepository(apiService)

        tmdb.getNowPlayingMovies(0, "").test {
            val error = awaitError() as HttpException
            assertEquals(500, error.response()!!.code())
        }
    }
}