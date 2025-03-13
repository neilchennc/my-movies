package tw.neilchen.sample.mymovies.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tw.neilchen.sample.mymovies.data.MovieCredits
import tw.neilchen.sample.mymovies.data.MovieDetail
import tw.neilchen.sample.mymovies.data.MovieImages
import tw.neilchen.sample.mymovies.data.MovieList
import tw.neilchen.sample.mymovies.data.MovieVideos
import tw.neilchen.sample.mymovies.data.PersonDetail
import tw.neilchen.sample.mymovies.data.PersonImages
import tw.neilchen.sample.mymovies.data.PersonMovieCredits

interface TmdbApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MovieList

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MovieList

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MovieList

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MovieList

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MovieList

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): MovieDetail

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String?
    ): MovieCredits

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int
    ): MovieImages

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int
    ): MovieVideos

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
//        @Query("year") year: Int?,
//        @Query("include_adult") includeAdult: Boolean? = false,
//        @Query("region") region: String?,
//        @Query("primary_release_year") primaryReleaseYear: Int?,
        @Query("language") language: String?
    ): MovieList

    @GET("person/{person_id}")
    suspend fun getPersonDetail(
        @Path("person_id") personId: Int,
        @Query("language") language: String?
    ): PersonDetail

    @GET("person/{person_id}/images")
    suspend fun getPersonImages(
        @Path("person_id") personId: Int
    ): PersonImages

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("person_id") personId: Int,
        @Query("language") language: String?
    ): PersonMovieCredits
}