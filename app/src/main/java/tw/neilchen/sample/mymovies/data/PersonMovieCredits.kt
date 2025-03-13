package tw.neilchen.sample.mymovies.data

import com.google.gson.annotations.SerializedName

data class PersonMovieCredits(
    @SerializedName("cast") val cast: List<Movie>,
    @SerializedName("crew") val crew: List<Movie>,
    @SerializedName("id") val id: Int
)