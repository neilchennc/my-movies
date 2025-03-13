package tw.neilchen.sample.mymovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import tw.neilchen.sample.mymovies.repository.PreferencesRepository
import tw.neilchen.sample.mymovies.ui.MyMoviesApp
import tw.neilchen.sample.mymovies.ui.util.LocaleUtil
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckAndUpdateLocale(preferences)
            MyMoviesApp()
        }
    }
}

@Composable
fun CheckAndUpdateLocale(preferences: PreferencesRepository) {
    val currentLanguageTag = LocaleUtil.getCurrentLanguageTag()
    val savedLanguageTag by preferences.languageTag.collectAsState(initial = null)
    if (currentLanguageTag != savedLanguageTag && savedLanguageTag != null) {
        LaunchedEffect(Unit) {
            preferences.updateLanguageTag(currentLanguageTag)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMoviesApp()
}