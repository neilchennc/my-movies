package tw.neilchen.sample.mymovies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import tw.neilchen.sample.mymovies.data.PersonDetail
import tw.neilchen.sample.mymovies.data.PersonImages
import tw.neilchen.sample.mymovies.data.PersonMovieCredits
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import javax.inject.Inject

sealed interface PersonDetailsUiState {

    data object Loading : PersonDetailsUiState

    data class Success(
        val personDetail: PersonDetail,
        val personImages: PersonImages,
        val personMovieCredits: PersonMovieCredits
    ) : PersonDetailsUiState

    data class Error(val throwable: Throwable) : PersonDetailsUiState
}

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    private val _personId = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _uiState =
        combine(
            preferencesRepository.languageTag,
            _personId
        ) { languageTag, personId ->
            Timber.d("Fetching person detail: personId: $personId")
            combine(
                moviesRepository.getPersonDetail(personId, languageTag),
                moviesRepository.getPersonImages(personId),
                moviesRepository.getPersonMovieCredits(personId, languageTag)
            ) { details, images, movieCredits ->
                Timber.d("Got person detail")
                PersonDetailsUiState.Success(
                    personDetail = details,
                    personImages = images,
                    personMovieCredits = movieCredits
                )
            }
        }.flatMapLatest<Flow<PersonDetailsUiState>, PersonDetailsUiState> {
            it
        }.catch { throwable ->
            Timber.d("Failed to fetch person details: $throwable")
            emit(PersonDetailsUiState.Error(throwable))
        }

    val uiState: StateFlow<PersonDetailsUiState> =
        _uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PersonDetailsUiState.Loading
        )

    fun loadPersonDetail(personId: Int) {
        Timber.d("old: ${_personId}, new: $personId")
        _personId.value = personId
    }
}