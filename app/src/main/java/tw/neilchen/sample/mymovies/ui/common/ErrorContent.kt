package tw.neilchen.sample.mymovies.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import retrofit2.HttpException
import tw.neilchen.sample.mymovies.R
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme
import java.io.IOException

@Composable
fun ErrorApiResponseContent(
    throwable: Throwable,
    modifier: Modifier = Modifier
) {
    // TODO: Add icon?
    if (throwable is IOException || throwable is HttpException) {
        Text(
            text = stringResource(R.string.error_network),
            modifier = modifier
        )
    } else {
        Text(
            text = "${stringResource(R.string.error)}: ${throwable.localizedMessage}",
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun NetworkErrorPreview() {
    MyMoviesTheme {
        ErrorApiResponseContent(
            throwable = IOException(""),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun UnknownThrowablePreview() {
    MyMoviesTheme {
        ErrorApiResponseContent(
            throwable = Throwable("Unknown Error"),
            modifier = Modifier.padding(16.dp)
        )
    }
}