package tw.neilchen.sample.mymovies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import javax.inject.Inject

sealed interface MoviesUiState {

    data object Loading : MoviesUiState

    data class Success(
        val trendingMovies: List<Movie>,
        val nowPlayingMovies: List<Movie>,
        val upcomingMovies: List<Movie>,
        val popularMovies: List<Movie>,
        val topRatedMovies: List<Movie>,
    ) : MoviesUiState

    data class Error(val throwable: Throwable) : MoviesUiState
}

@HiltViewModel
class MoviesViewModel @Inject constructor(
    moviesRepository: MoviesRepository,
    preferencesRepository: PreferencesRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val combinedData: Flow<MoviesUiState> =
        preferencesRepository.languageTag.flatMapLatest { languageTag ->
            combine(
                moviesRepository.getTrendingMoviesByDay(page = 1, language = languageTag),
                moviesRepository.getNowPlayingMovies(page = 1, language = languageTag),
                moviesRepository.getUpcomingMovies(page = 1, language = languageTag),
                moviesRepository.getPopularMovies(page = 1, language = languageTag),
                moviesRepository.getTopRatedMovies(page = 1, language = languageTag),
            ) { trending, nowPlaying, upcoming, popular, topRated ->
                Timber.d("Fetch movie lists success")
                MoviesUiState.Success(
                    trendingMovies = trending.results,
                    nowPlayingMovies = nowPlaying.results,
                    upcomingMovies = upcoming.results,
                    popularMovies = popular.results,
                    topRatedMovies = topRated.results
                )
            }.catch<MoviesUiState> { throwable ->
                Timber.d("Fetch movie lists failed: Throwable: $throwable")
                emit(MoviesUiState.Error(throwable = throwable))
            }
        }

    val uiState: StateFlow<MoviesUiState> =
        combinedData.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MoviesUiState.Loading
        )
}