package tw.neilchen.sample.mymovies.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tw.neilchen.sample.mymovies.data.Dummy
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.data.toFirstDecimalPlace
import tw.neilchen.sample.mymovies.data.toTmdbPosterUrl
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .width(114.dp)
            .clickable(
                enabled = true,
                onClick = { onMovieClick(movie) }
            )
    ) {
        MoviePosterImage(
            posterPath = movie.posterPath,
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            StarShape(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = movie.voteAverage.toFirstDecimalPlace(),
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Text(
            text = movie.title,
            fontSize = 14.sp,
            minLines = 2
        )
    }
}

@Composable
fun MoviePosterImage(
    posterPath: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(posterPath?.toTmdbPosterUrl() ?: "")
            .crossfade(true) // Animation
            .build(),
        //error = painterResource(R.drawable.ic_broken_image),
        //placeholder = painterResource(R.drawable.loading_img),
        contentDescription = "Poster",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .background(Color.LightGray)
            .aspectRatio(ratio = 2f / 3f)
    )
}

@Composable
fun StarShape(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .drawWithCache {
                val starPolygon = RoundedPolygon.Companion.star(
                    numVerticesPerRadius = 5,
                    radius = size.minDimension / 2f,
                    innerRadius = size.minDimension / 4f,
                    rounding = CornerRounding(
                        radius = 2f,
                        smoothing = 1f
                    ),
                    perVertexRounding = null,
                    innerRounding = null,
                    centerX = size.width / 2,
                    centerY = size.height / 2
                )
                val starPath = starPolygon.toPath().asComposePath()
                onDrawBehind {
                    rotate(degrees = 55f) {
                        drawPath(starPath, color = Color.Yellow)
                    }
                }
            }
    )
}

@Composable
fun VideoPlayImage(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Box(
        modifier = modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 3,
                    radius = size.minDimension / 2,
                    centerX = size.width / 2,
                    centerY = size.height / 2,
                    rounding = CornerRounding(
                        size.minDimension / 10f,
                        smoothing = 0.1f
                    )
                )
                val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                onDrawBehind {
                    drawCircle(
                        color = color,
                        style = Stroke(width = size.minDimension / 16)
                    )
                    drawPath(roundedPolygonPath, color)
                }
            }
    )
}

@Composable
fun SubtitleText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Preview
@Composable
private fun MovieItemPreview() {
    MyMoviesTheme {
        MovieItem(
            movie = Dummy.movieGodfather,
            onMovieClick = {},
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun StartShapePreview() {
    StarShape(
        modifier = Modifier.size(100.dp)
    )
}

@Preview
@Composable
private fun VideoPlayImagePreview() {
    VideoPlayImage(
        color = Color.Red,
        modifier = Modifier.size(100.dp)
    )
}
