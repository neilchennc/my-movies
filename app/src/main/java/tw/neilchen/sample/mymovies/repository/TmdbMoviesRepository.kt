package tw.neilchen.sample.mymovies.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.network.TmdbApiService
import tw.neilchen.sample.mymovies.paging.MoviesPagingSource
import javax.inject.Inject

class TmdbMoviesRepository @Inject constructor(
    private val service: TmdbApiService
) : MoviesRepository {

    override fun getNowPlayingMovies(page: Int, language: String) = flow {
        emit(service.getNowPlayingMovies(page, language))
    }.flowOn(Dispatchers.IO)

    override fun getUpcomingMovies(page: Int, language: String) = flow {
        emit(service.getUpcomingMovies(page, language))
    }.flowOn(Dispatchers.IO)

    override fun getPopularMovies(page: Int, language: String) = flow {
        emit(service.getPopularMovies(page, language))
    }.flowOn(Dispatchers.IO)

    override fun getTopRatedMovies(page: Int, language: String) = flow {
        emit(service.getTopRatedMovies(page, language))
    }.flowOn(Dispatchers.IO)

    override fun getTrendingMoviesByDay(page: Int, language: String) = flow {
        emit(service.getTrendingMovies("day", page, language))
    }.flowOn(Dispatchers.IO)

    override fun getTrendingMoviesByWeek(page: Int, language: String) = flow {
        emit(service.getTrendingMovies("week", page, language))
    }.flowOn(Dispatchers.IO)

    override fun getMovieDetails(movieId: Int, language: String) = flow {
        emit(service.getMovieDetails(movieId, language))
    }.flowOn(Dispatchers.IO)

    override fun getMovieCredits(movieId: Int, language: String) = flow {
        emit(service.getMovieCredits(movieId, language))
    }.flowOn(Dispatchers.IO)

    override fun getMovieImages(movieId: Int) = flow {
        emit(service.getMovieImages(movieId))
    }.flowOn(Dispatchers.IO)

    override fun getMovieVideos(movieId: Int) = flow {
        emit(service.getMovieVideos(movieId))
    }.flowOn(Dispatchers.IO)

    override fun searchMovies(query: String, language: String): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { MoviesPagingSource(service, query, language) }
        ).flow.flowOn(Dispatchers.IO)

    override fun getPersonDetail(personId: Int, language: String) = flow {
        emit(service.getPersonDetail(personId, language))
    }.flowOn(Dispatchers.IO)

    override fun getPersonImages(personId: Int) = flow {
        emit(service.getPersonImages(personId))
    }.flowOn(Dispatchers.IO)

    override fun getPersonMovieCredits(personId: Int, language: String) = flow {
        emit(service.getPersonMovieCredits(personId, language))
    }.flowOn(Dispatchers.IO)
}