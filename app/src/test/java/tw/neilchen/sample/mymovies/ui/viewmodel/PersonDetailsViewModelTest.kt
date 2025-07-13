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
import tw.neilchen.sample.mymovies.data.PersonDetail
import tw.neilchen.sample.mymovies.data.PersonImages
import tw.neilchen.sample.mymovies.data.PersonMovieCredits
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository

@OptIn(ExperimentalCoroutinesApi::class)
class PersonDetailsViewModelTest {

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    @MockK(relaxed = true)
    private lateinit var moviesRepository: MoviesRepository

    private lateinit var viewModel: PersonDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        MockKAnnotations.init(this)

        every { preferencesRepository.languageTag } returns flow { emit("zh-TW") }

        viewModel = PersonDetailsViewModel(moviesRepository, preferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getPersonDetails_success() = runTest {
        val personId = FakeData.personDetail.id
        val language = "zh-TW"

        every { preferencesRepository.languageTag } returns
                flow { emit(language) }
        every { moviesRepository.getPersonDetail(personId, language) } returns
                flow { emit(FakeData.personDetail) }
        every { moviesRepository.getPersonImages(personId) } returns
                flow { emit(PersonImages(id = personId, profiles = listOf(FakeData.personImage))) }
        every { moviesRepository.getPersonMovieCredits(personId, language) } returns
                flow { emit(FakeData.personMovieCredits) }

        viewModel.loadPersonDetail(personId)
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(PersonDetailsUiState.Loading)
            assertThat(awaitItem()).isEqualTo(
                PersonDetailsUiState.Success(
                    personDetail = FakeData.personDetail,
                    personImages = PersonImages(
                        id = personId,
                        profiles = listOf(FakeData.personImage)
                    ),
                    personMovieCredits = FakeData.personMovieCredits
                )
            )
        }
    }

    @Test
    fun getPersonDetails_networkError() = runTest {
        val personId = FakeData.personDetail.id
        val language = "zh-TW"
        val exception = HttpException(
            Response.error<Any>(
                500,
                "Internal error".toResponseBody("text/plain".toMediaType())
            )
        )

        every { preferencesRepository.languageTag } returns
                flow { emit(language) }
        every { moviesRepository.getPersonDetail(personId, language) } returns
                flow { throw exception }
//        every { moviesRepository.getPersonImages(personId) } returns
//                flow { emit(PersonImages(id = personId, profiles = listOf(FakeData.personImage))) }
//        every { moviesRepository.getPersonMovieCredits(personId, language) } returns
//                flow { emit(FakeData.personMovieCredits) }

        viewModel.loadPersonDetail(personId)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(PersonDetailsUiState.Loading)
            assertThat(awaitItem()).isEqualTo(PersonDetailsUiState.Error(exception))
        }
    }
}