package tw.neilchen.sample.mymovies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.neilchen.sample.mymovies.database.AppDatabase
import tw.neilchen.sample.mymovies.database.SearchKeywordDao
import tw.neilchen.sample.mymovies.dispatcher.MockServerDispatcher
import tw.neilchen.sample.mymovies.network.AuthInterceptor
import tw.neilchen.sample.mymovies.network.TmdbApiService
import tw.neilchen.sample.mymovies.repository.AppDatabaseRepository
import tw.neilchen.sample.mymovies.repository.DatabaseRepository
import tw.neilchen.sample.mymovies.repository.MoviesRepository
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import tw.neilchen.sample.mymovies.repository.TmdbMoviesRepository
import tw.neilchen.sample.mymovies.repository.UserPreferencesRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    //private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user")

    private fun getBaseUrl(mockWebServer: MockWebServer) = runBlocking(Dispatchers.IO) {
        mockWebServer.url("/")
    }

    @Singleton
    @Provides
    fun provideTmdbService(retrofit: Retrofit): TmdbApiService {
        return retrofit.create(TmdbApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        mockWebServer: MockWebServer
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(getBaseUrl(mockWebServer))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMockWebServer(): MockWebServer {
        return MockWebServer()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return AuthInterceptor()
    }

    @Provides
    @Singleton
    fun provideMoviesRepository(service: TmdbApiService): MoviesRepository {
        return TmdbMoviesRepository(service)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(dataStore: DataStore<Preferences>): PreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchKeywordDao(appDatabase: AppDatabase): SearchKeywordDao {
        return appDatabase.searchKeywordDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabaseRepository(searchKeywordDao: SearchKeywordDao): DatabaseRepository {
        return AppDatabaseRepository(searchKeywordDao)
    }
}