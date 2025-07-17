package tw.neilchen.sample.mymovies.dispatcher

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher : Dispatcher() {

    private val assets = InstrumentationRegistry.getInstrumentation().context.assets

    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path.orEmpty()
        return when {
            path.startsWith("/movie/now_playing") -> createResponse("now_playing_movies.json")
            path.startsWith("/movie/upcoming") -> createResponse("upcoming_movies.json")
            path.startsWith("/movie/popular") -> createResponse("popular_movies.json")
            path.startsWith("/movie/top_rated") -> createResponse("top_rated_movies.json")
            path.startsWith("/trending/movie/") -> createResponse("trending_movies.json")
            Regex("""^/movie/\d+/credits.*""").matches(path) -> createResponse("movie_credits.json")
            Regex("""^/movie/\d+/images.*""").matches(path) -> createResponse("movie_images.json")
            Regex("""^/movie/\d+/videos.*""").matches(path) -> createResponse("movie_videos.json")
            Regex("""^/movie/\d+.*""").matches(path) -> createResponse("movie_detail.json")
            else -> {
                MockResponse().setResponseCode(404).setBody("Not Found.")
            }
        }
    }

    private fun createResponse(fileName: String): MockResponse {
        return MockResponse().setBody(getJsonFromAssets(fileName))
    }

    private fun getJsonFromAssets(fileName: String): String {
        return assets.open(fileName).bufferedReader().use { it.readText() }
    }
}