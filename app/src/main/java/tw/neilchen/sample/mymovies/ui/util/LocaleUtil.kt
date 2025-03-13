package tw.neilchen.sample.mymovies.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.os.ConfigurationCompat

object LocaleUtil {
    @Composable
    @ReadOnlyComposable
    fun getCurrentLanguageTag(): String {
        val locale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0]
        return if (locale == null) {
            "en-US"
        } else {
            "${locale.language}-${locale.country}"
        }
    }
}