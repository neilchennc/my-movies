package tw.neilchen.sample.mymovies.ui.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.data.MovieList
import tw.neilchen.sample.mymovies.data.SearchKeyword
import tw.neilchen.sample.mymovies.network.TmdbApiService
import tw.neilchen.sample.mymovies.paging.MoviesPagingSource
import tw.neilchen.sample.mymovies.repository.DatabaseRepository
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesViewModelTest {

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK
    private lateinit var apiService: TmdbApiService

    @MockK(relaxed = true)
    private lateinit var moviesRepository: MoviesRepository

    @MockK(relaxed = true)
    private lateinit var databaseRepository: DatabaseRepository

    private lateinit var viewModel: SearchMoviesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())

        every { preferencesRepository.languageTag } returns flow { emit("") }

        viewModel = SearchMoviesViewModel(moviesRepository, preferencesRepository, databaseRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchMovies_successResult() = runTest {
        val result = listOf(FakeData.movie)
        val pager = Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = result.asPagingSourceFactory()
        )

        every { preferencesRepository.languageTag } returns flowOf("")
        every { moviesRepository.searchMovies(any(), any()) } returns pager.flow

        viewModel.searchMovies("")

        val snapshot = viewModel.resultFlow.asSnapshot()
        assertThat(snapshot).isEqualTo(result)
    }

    @Test
    fun searchMovies_networkError() = runTest {
        val pager = Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { MoviesPagingSource(apiService, "123", "") }
        )
        val exception = HttpException(
            Response.error<Any>(
                500,
                "Internal error".toResponseBody("text/plain".toMediaType())
            )
        )

        every { preferencesRepository.languageTag } returns flowOf("")
        coEvery { apiService.searchMovies(any(), any(), "") } throws exception
        every { moviesRepository.searchMovies(any(), any()) } returns pager.flow

        viewModel.searchMovies("")

        try {
            viewModel.resultFlow.asSnapshot()
            throw Exception("Test should not be reached here")
        } catch (e: HttpException) {
            assertThat(e).isEqualTo(exception)
        }
    }

    @Test
    fun searchMovies_insertSearchKeywords() = runTest {
        val keyword = "Hello World"

        coEvery { databaseRepository.insertSearchKeyword(any()) } returns Unit

        // FIXME: WTF cannot be mocked by MockK?
        every { databaseRepository.getAllSearchKeywords() } returns flow {
            emit(listOf(SearchKeyword(id = 0, keyword = keyword, addedAt = Date())))
        }

        viewModel.insertSearchKeyword(keyword)

        viewModel.keywordsFlow.test {
            assertThat(awaitItem()).isEqualTo(keyword)
        }

        coVerify { databaseRepository.insertSearchKeyword(any()) }
    }
}