package tw.neilchen.sample.mymovies.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class UserPreferencesRepositoryTest {

    @get:Rule
    val temporaryFolder: TemporaryFolder = TemporaryFolder.builder()
        .assureDeletion()
        .build()

    @Test
    fun setLanguageTag_isCorrect() = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { temporaryFolder.newFile("test.preferences_pb") },
        )
        val storage = UserPreferencesRepository(dataStore)
        storage.updateLanguageTag("zh-TW")
        storage.languageTag.test {
            assertThat(awaitItem()).isEqualTo("zh-TW")
        }
    }
}