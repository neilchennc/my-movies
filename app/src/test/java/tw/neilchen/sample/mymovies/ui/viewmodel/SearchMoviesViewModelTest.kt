package tw.neilchen.sample.mymovies.ui.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import tw.neilchen.sample.mymovies.data.FakeData
import tw.neilchen.sample.mymovies.repository.DatabaseRepository
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesViewModelTest {

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK(relaxed = true)
    private lateinit var moviesRepository: MoviesRepository

    @MockK(relaxed = true)
    private lateinit var databaseRepository: DatabaseRepository

    private lateinit var viewModel: SearchMoviesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { preferencesRepository.languageTag } returns flow { emit("") }

        viewModel = SearchMoviesViewModel(moviesRepository, preferencesRepository, databaseRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun searchMovies_successResult() = runTest {
        val query = "god"
        val result = listOf(FakeData.movie)
        val pager = Pager(
            config = PagingConfig(1),
            pagingSourceFactory = result.asPagingSourceFactory()
        )

        every { preferencesRepository.languageTag } returns flowOf("")
        every { moviesRepository.searchMovies(query, "") } returns pager.flow

        viewModel.searchMovies(query)

        val snapshot = viewModel.resultFlow.asSnapshot()
        assertThat(snapshot).isEqualTo(result)
    }

    @Test
    fun searchMovies_networkError() = runTest {
        // TODO
    }

    @Test
    fun searchMovies_insertSearchKeywords() = runTest {
        // TODO
    }
}