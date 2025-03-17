package tw.neilchen.sample.mymovies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.data.SearchKeyword
import tw.neilchen.sample.mymovies.repository.DatabaseRepository
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import java.util.Date
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchMoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val preferencesRepository: PreferencesRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    val resultFlow: Flow<PagingData<Movie>> =
        query.combine(preferencesRepository.languageTag) { query, language ->
            moviesRepository.searchMovies(query = query, language = language)
        }.flatMapLatest {
            it
        }.cachedIn(viewModelScope)

    val keywordsFlow: StateFlow<List<String>> =
        databaseRepository.getAllSearchKeywords()
            .map { searchKeyword ->
                searchKeyword.map { it.keyword }
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    fun searchMovies(keyword: String) {
        if (keyword.isNotEmpty()) {
            query.value = keyword
        }
    }

    fun insertSearchKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertSearchKeyword(
                SearchKeyword(
                    keyword = keyword,
                    addedAt = Date()
                )
            )
        }
    }
}