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
import tw.neilchen.sample.mymovies.data.MovieCredits
import tw.neilchen.sample.mymovies.data.MovieDetail
import tw.neilchen.sample.mymovies.data.MovieImages
import tw.neilchen.sample.mymovies.data.MovieVideos
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import javax.inject.Inject

sealed interface MovieDetailsUiState {

    data object Loading : MovieDetailsUiState

    data class Success(
        val movieDetail: MovieDetail,
        val movieCredits: MovieCredits,
        val movieImages: MovieImages,
        val movieVideos: MovieVideos
    ) : MovieDetailsUiState

    data class Error(val throwable: Throwable) : MovieDetailsUiState
}

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _movieId = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _uiState: Flow<MovieDetailsUiState> =
        combine(
            preferencesRepository.languageTag,
            _movieId
        ) { languageTag, movieId ->
            Timber.d("Fetching movie detail: id=$movieId, language=$languageTag")
            combine(
                moviesRepository.getMovieDetails(movieId = movieId, language = languageTag),
                moviesRepository.getMovieCredits(movieId = movieId, language = languageTag),
                moviesRepository.getMovieImages(movieId = movieId),
                moviesRepository.getMovieVideos(movieId = movieId)
            ) { details, credits, images, videos ->
                Timber.d("Got movie details")
                MovieDetailsUiState.Success(
                    movieDetail = details,
                    movieCredits = credits,
                    movieImages = images,
                    movieVideos = videos
                )
            }
        }.flatMapLatest<Flow<MovieDetailsUiState>, MovieDetailsUiState> {
            it
        }.catch { throwable ->
            Timber.d("Failed fetching movie detail. Throwable: $throwable")
            emit(MovieDetailsUiState.Error(throwable))
        }

    val uiState: StateFlow<MovieDetailsUiState> =
        _uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MovieDetailsUiState.Loading
        )

    fun loadMovieDetails(movieId: Int) {
        Timber.d("old=${_movieId.value}, new=${movieId}")
        _movieId.value = movieId
    }
}