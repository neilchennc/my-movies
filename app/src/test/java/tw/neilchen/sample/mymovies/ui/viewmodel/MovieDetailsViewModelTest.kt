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
import tw.neilchen.sample.mymovies.data.FakeData
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK(relaxed = true)
    private lateinit var moviesRepository: MoviesRepository

    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { preferencesRepository.languageTag } returns flow { emit("zh-TW") }

        viewModel = MovieDetailsViewModel(moviesRepository, preferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getMovieDetails_success() = runTest {
        val movieId = FakeData.movieVideos.id
        val language = "zh-TW"

        every { preferencesRepository.languageTag } returns
                flow { emit(language) }
        every { moviesRepository.getMovieDetails(movieId, language) } returns
                flow { emit(FakeData.movieDetail) }
        every { moviesRepository.getMovieCredits(movieId, language) } returns
                flow { emit(FakeData.movieCredits) }
        every { moviesRepository.getMovieImages(movieId) } returns
                flow { emit(FakeData.movieImages) }
        every { moviesRepository.getMovieVideos(movieId) } returns
                flow { emit(FakeData.movieVideos) }

        viewModel.loadMovieDetails(movieId)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiState.Loading)
            assertThat(awaitItem()).isEqualTo(
                MovieDetailsUiState.Success(
                    movieDetail = FakeData.movieDetail,
                    movieCredits = FakeData.movieCredits,
                    movieImages = FakeData.movieImages,
                    movieVideos = FakeData.movieVideos
                )
            )
        }
    }

    @Test
    fun getMovieDetails_networkError() = runTest {
        val movieId = FakeData.movieVideos.id
        val language = "zh-TW"
        val exception = HttpException(
            Response.error<Any>(
                500,
                "Internal error".toResponseBody("text/plain".toMediaType())
            )
        )

        every { preferencesRepository.languageTag } returns
                flow { emit(language) }
        every { moviesRepository.getMovieDetails(movieId, language) } returns
                flow { throw exception }
//        every { moviesRepository.getMovieCredits(movieId, language) } returns
//                flow { emit(FakeData.movieCredits) }
//        every { moviesRepository.getMovieImages(movieId) } returns
//                flow { emit(FakeData.movieImages) }
//        every { moviesRepository.getMovieVideos(movieId) } returns
//                flow { emit(FakeData.movieVideos) }

        viewModel.loadMovieDetails(movieId)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiState.Loading)
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiState.Error(exception))
        }
    }
}