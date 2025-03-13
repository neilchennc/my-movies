package tw.neilchen.sample.mymovies.data

import com.google.gson.annotations.SerializedName

data class PersonImages(
    @SerializedName("id") val id: Int,
    @SerializedName("profiles") val profiles: List<ProfileImage>
)

data class ProfileImage(
    @SerializedName("aspect_ratio") val aspectRatio: Float,
    @SerializedName("height") val height: Int,
    @SerializedName("iso_639_1") val iso6391: String,
    @SerializedName("file_path") val filePath: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("width") val width: Int
)