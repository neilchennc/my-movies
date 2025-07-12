package tw.neilchen.sample.mymovies.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserPreferencesRepositoryTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: UserPreferencesRepository

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dataStore = PreferenceDataStoreFactory.create(
            //scope = testCoroutineScope,
            produceFile = {
                context.preferencesDataStoreFile("test_datastore")
            }
        )
        repository = UserPreferencesRepository(dataStore)
    }

    @After
    fun teardown() = runTest {
        dataStore.edit { it.clear() }
    }

    @Test
    fun setLanguageTag_isCorrect() = runTest {
        // Check default value is empty
        repository.languageTag.test {
            Truth.assertThat(awaitItem()).isEmpty()
        }

        // Set language tag
        repository.updateLanguageTag("zh-TW")

        // Check language tag
        repository.languageTag.test {
            Truth.assertThat(awaitItem()).isEqualTo("zh-TW")
        }
    }
}