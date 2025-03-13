package tw.neilchen.sample.mymovies.data

import java.util.Locale

/**
 * Available poster sizes:
 *
 * w92,
 * w154,
 * w185,
 * w342,
 * w500,
 * w780,
 * original
 *
 * Configuration: https://developer.themoviedb.org/reference/configuration-details
 */
fun String?.toTmdbPosterUrl(): String {
    return if (this != null) {
        "https://image.tmdb.org/t/p/w342/$this"
    } else {
        ""
    }
}

/**
 * Available poster sizes:
 *
 * w92,
 * w154,
 * w185,
 * w342,
 * w500,
 * w780,
 * original
 *
 * Configuration: https://developer.themoviedb.org/reference/configuration-details
 */
fun String?.toTmdbPosterOriginalUrl(): String {
    return if (this != null) {
        "https://image.tmdb.org/t/p/original/$this"
    } else {
        ""
    }
}

/**
 * Available backdrop sizes:
 *
 * w300,
 * w780,
 * w1280,
 * original
 *
 * Configuration: https://developer.themoviedb.org/reference/configuration-details
 */
fun String?.toTmdbBackdrop1280Url(): String {
    return if (this != null) {
        "https://image.tmdb.org/t/p/w1280/$this"
    } else {
        ""
    }
}

/**
 * Available backdrop sizes:
 *
 * w300,
 * w780,
 * w1280,
 * original
 *
 * Configuration: https://developer.themoviedb.org/reference/configuration-details
 */
fun String?.toTmdbBackdrop780Url(): String {
    return if (this != null) {
        "https://image.tmdb.org/t/p/w780/$this"
    } else {
        ""
    }
}

/**
 * Available profile sizes:
 *
 * w45,
 * w185,
 * h632,
 * original
 *
 * Configuration: https://developer.themoviedb.org/reference/configuration-details
 */
fun String?.toTmdbProfileUrl(): String {
    return if (this != null) {
        "https://image.tmdb.org/t/p/w185/$this"
    } else {
        ""
    }
}

/**
 * Available profile sizes:
 *
 * w45,
 * w185,
 * h632,
 * original
 *
 * Configuration: https://developer.themoviedb.org/reference/configuration-details
 */
fun String?.toTmdbProfileOriginalUrl(): String {
    return if (this != null) {
        "https://image.tmdb.org/t/p/original/$this"
    } else {
        ""
    }
}

fun String.toYouTubeUrl(): String {
    return "https://www.youtube.com/watch?v=$this"
}

fun Double?.toFirstDecimalPlace(): String {
    return String.format(Locale.getDefault(), "%.1f", this)
}

fun MovieDetail.formatGenresString(): String {
    return if (this.genres.isNotEmpty()) {
        val builder = StringBuilder()
        val iterator = this.genres.iterator()
        while (iterator.hasNext()) {
            builder.append(iterator.next().name)
            if (iterator.hasNext()) {
                builder.append("．")
            }
        }
        builder.toString()
    } else {
        ""
    }
}

fun MovieDetail.formatSpokenLanguagesString(): String {
    return if (this.spokenLanguages.isNotEmpty()) {
        val builder = StringBuilder()
        val iterator = this.spokenLanguages.iterator()
        while (iterator.hasNext()) {
            builder.append(iterator.next().name)
            if (iterator.hasNext()) {
                builder.append("．")
            }
        }
        builder.toString()
    } else {
        ""
    }
}