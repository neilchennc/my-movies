package tw.neilchen.sample.mymovies.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Column(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = "Error Icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(48.dp)
        )
        when (throwable) {
            is IOException, is HttpException -> {
                Text(
                    text = stringResource(R.string.error_network),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            else -> {
                Text(
                    text = "${stringResource(R.string.error)}: ${throwable.localizedMessage}",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun ErrorLoadingImageContent(
    throwable: Throwable,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = "Error Icon",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(48.dp)
        )
        Text(
            text = "${stringResource(R.string.failed_loading_image)}: ${throwable.localizedMessage}",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
private fun ApiNetworkErrorPreview() {
    MyMoviesTheme {
        ErrorApiResponseContent(
            throwable = IOException(""),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun ApiUnknownThrowablePreview() {
    MyMoviesTheme {
        ErrorApiResponseContent(
            throwable = Throwable("Unknown Error"),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun ImageErrorPreview() {
    MyMoviesTheme {
        ErrorLoadingImageContent(
            throwable = IOException("HTTP 404"),
            modifier = Modifier.padding(16.dp)
        )
    }
}