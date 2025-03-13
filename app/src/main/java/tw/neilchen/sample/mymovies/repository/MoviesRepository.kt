package tw.neilchen.sample.mymovies.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.data.MovieCredits
import tw.neilchen.sample.mymovies.data.MovieDetail
import tw.neilchen.sample.mymovies.data.MovieImages
import tw.neilchen.sample.mymovies.data.MovieList
import tw.neilchen.sample.mymovies.data.MovieVideos
import tw.neilchen.sample.mymovies.data.PersonDetail
import tw.neilchen.sample.mymovies.data.PersonImages
import tw.neilchen.sample.mymovies.data.PersonMovieCredits

interface MoviesRepository {

    fun getNowPlayingMovies(page: Int, language: String): Flow<MovieList>

    fun getUpcomingMovies(page: Int, language: String): Flow<MovieList>

    fun getPopularMovies(page: Int, language: String): Flow<MovieList>

    fun getTopRatedMovies(page: Int, language: String): Flow<MovieList>

    fun getTrendingMoviesByDay(page: Int, language: String): Flow<MovieList>

    fun getTrendingMoviesByWeek(page: Int, language: String): Flow<MovieList>

    fun getMovieDetails(movieId: Int, language: String): Flow<MovieDetail>

    fun getMovieCredits(movieId: Int, language: String): Flow<MovieCredits>

    fun getMovieImages(movieId: Int): Flow<MovieImages>

    fun getMovieVideos(movieId: Int): Flow<MovieVideos>

    fun searchMovies(query: String, language: String): Flow<PagingData<Movie>>

    fun getPersonDetail(personId: Int, language: String): Flow<PersonDetail>

    fun getPersonImages(personId: Int): Flow<PersonImages>

    fun getPersonMovieCredits(personId: Int, language: String): Flow<PersonMovieCredits>
}