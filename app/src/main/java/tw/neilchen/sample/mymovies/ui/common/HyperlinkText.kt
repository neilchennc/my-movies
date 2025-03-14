package tw.neilchen.sample.mymovies.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme

@Composable
fun HyperlinkText(
    text: String,
    url: String
) {
    Text(
        buildAnnotatedString {
            withLink(
                LinkAnnotation.Url(
                    url = url,
                    styles = TextLinkStyles(style = SpanStyle(color = Color.Blue))
                )
            ) {
                append(text)
            }
        }
    )
}

@Preview
@Composable
private fun HyperlinkTextPreview() {
    MyMoviesTheme {
        HyperlinkText(text = "Hello World", url = "https://www.example.com/")
    }
}