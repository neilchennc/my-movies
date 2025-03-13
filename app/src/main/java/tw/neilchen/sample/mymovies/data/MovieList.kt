package tw.neilchen.sample.mymovies.data

import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("dates") val dates: Dates,
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("results") val results: List<Movie>
)

data class Dates(
    val maximum: String,
    val minimum: String,
)