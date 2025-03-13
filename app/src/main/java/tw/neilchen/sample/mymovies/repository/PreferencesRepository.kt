package tw.neilchen.sample.mymovies.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val languageTag: Flow<String>
    suspend fun updateLanguageTag(languageTag: String)
}