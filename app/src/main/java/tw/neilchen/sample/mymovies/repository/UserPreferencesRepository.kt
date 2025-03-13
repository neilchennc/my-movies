package tw.neilchen.sample.mymovies.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    companion object {
        val LANGUAGE_TAG = stringPreferencesKey("language_tag")
    }

    override val languageTag: Flow<String> =
        dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            Timber.d("read language tag: ${preferences[LANGUAGE_TAG]}")
            preferences[LANGUAGE_TAG] ?: ""
        }

    override suspend fun updateLanguageTag(languageTag: String) {
        dataStore.edit { preferences ->
            Timber.d("save language tag: $languageTag")
            preferences[LANGUAGE_TAG] = languageTag
        }
    }
}