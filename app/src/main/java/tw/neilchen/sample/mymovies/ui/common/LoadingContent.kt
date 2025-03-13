package tw.neilchen.sample.mymovies.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme

@Composable
fun CircularProgressLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Preview
@Composable
private fun CircularProgressLoadingPreview() {
    MyMoviesTheme {
        CircularProgressLoading()
    }
}