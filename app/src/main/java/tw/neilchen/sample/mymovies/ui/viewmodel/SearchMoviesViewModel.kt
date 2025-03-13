package tw.neilchen.sample.mymovies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchMoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    val resultFlow: Flow<PagingData<Movie>> =
        query.combine(preferencesRepository.languageTag) { query, language ->
                moviesRepository.searchMovies(query = query, language = language)
            }.flatMapLatest {
                it
            }.cachedIn(viewModelScope)

    fun searchMovies(keyword: String) {
        if (keyword.isNotEmpty()) {
            query.value = keyword
        }
    }
}