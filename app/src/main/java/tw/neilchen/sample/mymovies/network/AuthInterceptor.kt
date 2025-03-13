package tw.neilchen.sample.mymovies.network

import okhttp3.Interceptor
import okhttp3.Response
import tw.neilchen.sample.mymovies.BuildConfig

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = BuildConfig.TMDB_ACCESS_TOKEN
        request.addHeader("Authorization", "Bearer $token")
        request.addHeader("Accept", "application/json")
        return chain.proceed(request.build())
    }
}