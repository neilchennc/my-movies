package tw.neilchen.sample.mymovies.ui.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import tw.neilchen.sample.mymovies.data.Dates
import tw.neilchen.sample.mymovies.data.FakeData
import tw.neilchen.sample.mymovies.data.MovieList
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK(relaxed = true)
    private lateinit var moviesRepository: MoviesRepository

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { preferencesRepository.languageTag } returns flow { emit("") }

        viewModel = MoviesViewModel(moviesRepository, preferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getMovies_LoadingAndSuccessState() = runTest {
        val movieList = MovieList(Dates("", ""), 1, 1, 0, listOf(FakeData.movie))

        every { preferencesRepository.languageTag } returns flow { emit("zh-TW") }
        every { moviesRepository.getTrendingMoviesByDay(any(), any()) } returns flow { emit(movieList) }
        every { moviesRepository.getNowPlayingMovies(any(), any()) } returns flow { emit(movieList) }
        every { moviesRepository.getUpcomingMovies(any(), any()) } returns flow { emit(movieList) }
        every { moviesRepository.getPopularMovies(any(), any()) } returns flow { emit(movieList) }
        every { moviesRepository.getTopRatedMovies(any(), any()) } returns flow { emit(movieList) }

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(MoviesUiState.Loading)
            assertThat(awaitItem()).isEqualTo(
                MoviesUiState.Success(
                    trendingMovies = movieList.results,
                    nowPlayingMovies = movieList.results,
                    upcomingMovies = movieList.results,
                    popularMovies = movieList.results,
                    topRatedMovies = movieList.results
                )
            )
        }
    }

    @Test
    fun getMovies_networkError() = runTest {
        val exception = HttpException(
            Response.error<Any>(
                500,
                "Internal error".toResponseBody("text/plain".toMediaType())
            )
        )

        every { preferencesRepository.languageTag } returns flow { emit("") }
        every { moviesRepository.getTrendingMoviesByDay(any(), any()) } returns flow { throw exception }
        every { moviesRepository.getNowPlayingMovies(any(), any()) } returns flow { throw exception }
        every { moviesRepository.getUpcomingMovies(any(), any()) } returns flow { throw exception }
        every { moviesRepository.getPopularMovies(any(), any()) } returns flow { throw exception }
        every { moviesRepository.getTopRatedMovies(any(), any()) } returns flow { throw exception }

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(MoviesUiState.Loading)
            assertThat(awaitItem()).isEqualTo(MoviesUiState.Error(exception))
        }
    }
}