package tw.neilchen.sample.mymovies.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import tw.neilchen.sample.mymovies.R
import tw.neilchen.sample.mymovies.data.Backdrop
import tw.neilchen.sample.mymovies.data.Cast
import tw.neilchen.sample.mymovies.data.Crew
import tw.neilchen.sample.mymovies.data.Dummy
import tw.neilchen.sample.mymovies.data.MovieCredits
import tw.neilchen.sample.mymovies.data.MovieDetail
import tw.neilchen.sample.mymovies.data.MovieImages
import tw.neilchen.sample.mymovies.data.MovieVideos
import tw.neilchen.sample.mymovies.data.Video
import tw.neilchen.sample.mymovies.data.formatGenresString
import tw.neilchen.sample.mymovies.data.formatSpokenLanguagesString
import tw.neilchen.sample.mymovies.data.toFirstDecimalPlace
import tw.neilchen.sample.mymovies.data.toTmdbBackdrop1280Url
import tw.neilchen.sample.mymovies.data.toTmdbBackdrop780Url
import tw.neilchen.sample.mymovies.data.toTmdbPosterOriginalUrl
import tw.neilchen.sample.mymovies.data.toTmdbProfileUrl
import tw.neilchen.sample.mymovies.ui.common.CircularProgressLoading
import tw.neilchen.sample.mymovies.ui.common.ErrorApiResponseContent
import tw.neilchen.sample.mymovies.ui.common.MoviePosterImage
import tw.neilchen.sample.mymovies.ui.common.StarShape
import tw.neilchen.sample.mymovies.ui.common.SubtitleText
import tw.neilchen.sample.mymovies.ui.common.VideoPlayImage
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme
import tw.neilchen.sample.mymovies.ui.util.TestTags
import tw.neilchen.sample.mymovies.ui.viewmodel.MovieDetailsUiState
import tw.neilchen.sample.mymovies.ui.viewmodel.MovieDetailsViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onImageClick: (images: List<String>, clickedIndex: Int) -> Unit,
    onVideoClick: (key: String) -> Unit,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMovieDetails(movieId)
    }

    when (uiState) {
        is MovieDetailsUiState.Loading -> {
            MovieDetailLoadingContent(modifier = modifier)
        }

        is MovieDetailsUiState.Success -> {
            val success = uiState as MovieDetailsUiState.Success
            MovieDetailContent(
                movieDetail = success.movieDetail,
                movieCredits = success.movieCredits,
                movieImages = success.movieImages,
                movieVideos = success.movieVideos,
                onImageClick = onImageClick,
                onVideoClick = onVideoClick,
                onPersonClick = onPersonClick,
                modifier = modifier
            )
        }

        is MovieDetailsUiState.Error -> {
            val error = uiState as MovieDetailsUiState.Error
            MovieDetailErrorContent(throwable = error.throwable, modifier = modifier)
        }
    }
}

@Composable
fun MovieDetailLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressLoading(modifier = modifier.align(Alignment.Center))
    }
}

@Composable
fun MovieDetailErrorContent(modifier: Modifier = Modifier, throwable: Throwable) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ErrorApiResponseContent(
            throwable = throwable,
            modifier = modifier
                .align(Alignment.Center)
                .padding(16.dp)
        )
    }
}

@Composable
fun MovieDetailContent(
    movieDetail: MovieDetail,
    movieCredits: MovieCredits,
    movieImages: MovieImages,
    movieVideos: MovieVideos,
    onImageClick: (images: List<String>, clickedIndex: Int) -> Unit,
    onVideoClick: (key: String) -> Unit,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag(TestTags.MOVIE_DETAIL_CONTENT)
    ) {
        MovieBackdropImage(
            movieDetail = movieDetail,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            MovieDetailBasic(
                movieDetail = movieDetail,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            MoviePosterImage(
                posterPath = movieDetail.posterPath,
                modifier = Modifier
                    .width(114.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        enabled = true,
                        onClick = {
                            val images = listOf(movieDetail.posterPath.toTmdbPosterOriginalUrl())
                            val clickedIndex = 0
                            onImageClick(images, clickedIndex)
                        }
                    )
                    .testTag(TestTags.MOVIE_POSTER_IMAGE)
            )
        }

        if (movieDetail.tagline.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movieDetail.tagline,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                modifier = Modifier
                    .alpha(.5f)
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }

        SubtitleText(
            title = stringResource(R.string.movie_overview),
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )

        Text(
            text = movieDetail.overview,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )

        MovieCastsContent(
            casts = movieCredits.cast,
            onPersonClick = onPersonClick,
            modifier = Modifier.fillMaxWidth()
        )

        MovieCrewsContent(
            crews = movieCredits.crew,
            onPersonClick = onPersonClick,
            modifier = Modifier.fillMaxWidth()
        )

        MovieBackdropImagesContent(
            backdrops = movieImages.backdrops,
            onImageClick = onImageClick,
            modifier = Modifier.fillMaxWidth()
        )

        MovieVideosContent(
            videos = movieVideos.results,
            onVideoClick = onVideoClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun MovieDetailBasic(
    movieDetail: MovieDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = movieDetail.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        if (movieDetail.title != movieDetail.originalTitle) {
            Text(
                text = " (${movieDetail.originalTitle})",
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.alpha(.5f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            StarShape(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "${stringResource(R.string.movie_rate)}: ${movieDetail.voteAverage.toFirstDecimalPlace()}"
            )
        }

        Row {
            Text(text = "${stringResource(R.string.movie_release_date)}: ${movieDetail.releaseDate}")
            if (movieDetail.productionCompanies.isNotEmpty()) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "(${movieDetail.productionCompanies[0].originCountry})")
            }
        }

        Row {
            Text(text = "${stringResource(R.string.movie_genre)}: ")
            Text(text = movieDetail.formatGenresString())
        }

        Row {
            Text(text = "${stringResource(R.string.movie_spoken_language)}: ")
            Text(text = movieDetail.formatSpokenLanguagesString())
        }
    }
}

@Composable
fun MovieBackdropImage(
    movieDetail: MovieDetail,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(movieDetail.backdropPath.toTmdbBackdrop1280Url())
            .crossfade(true) // Animation
            .build(),
        //error = painterResource(R.drawable.ic_broken_image),
        //placeholder = painterResource(R.drawable.loading_img),
        contentDescription = "Backdrop ${movieDetail.title}",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .background(Color.LightGray)
            .testTag(TestTags.MOVIE_BACKDROP_IMAGE)
    )
}

@Composable
fun MovieCastsContent(
    casts: List<Cast>,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubtitleText(
            title = stringResource(R.string.movie_cast),
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(modifier = modifier) {
            items(casts) { cast ->
                MovieCastItem(
                    cast = cast,
                    onPersonClick = onPersonClick,
                    modifier = Modifier
                        .width(96.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun MovieCastItem(
    cast: Cast,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    PersonItem(
        id = cast.id,
        name = cast.name,
        imageUrl = cast.profilePath.toTmdbProfileUrl(),
        onPersonClick = onPersonClick,
        modifier = modifier
    )
}

@Composable
fun MovieCrewsContent(
    crews: List<Crew>,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubtitleText(
            title = stringResource(R.string.movie_crew),
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(modifier = modifier) {
            items(crews) { crew ->
                MovieCrewItem(
                    crew = crew,
                    onPersonClick = onPersonClick,
                    modifier = Modifier
                        .width(96.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun MovieBackdropImagesContent(
    backdrops: List<Backdrop>,
    onImageClick: (images: List<String>, clickedIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubtitleText(
            title = stringResource(R.string.movie_backdrop_image),
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(modifier = Modifier) {
            items(backdrops) { backdrop ->
                MovieBackdropImage(
                    backdrops = backdrops,
                    backdrop = backdrop,
                    onImageClick = onImageClick,
                    modifier = Modifier
                        .width(320.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun MovieVideosContent(
    videos: List<Video>,
    onVideoClick: (key: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.testTag(TestTags.MOVIE_VIDEO_CONTENT)
    ) {
        SubtitleText(
            title = stringResource(R.string.movie_video),
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(modifier = modifier) {
            items(videos) { video ->
                MovieVideoThumbnailItem(
                    key = video.key,
                    name = video.name,
                    onVideoClick = onVideoClick,
                    modifier = Modifier
                        .width(320.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun MovieCrewItem(
    crew: Crew,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    PersonItem(
        id = crew.id,
        name = crew.name,
        imageUrl = crew.profilePath.toTmdbProfileUrl(),
        onPersonClick = onPersonClick,
        modifier = modifier
    )
}

@Composable
fun PersonItem(
    id: Int,
    name: String,
    imageUrl: String,
    onPersonClick: (personId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(
            enabled = true,
            onClick = { onPersonClick(id) }
        )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imageUrl)
                .build(),
            //error = painterResource(R.drawable.ic_broken_image),
            //placeholder = painterResource(R.drawable.loading_img),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .background(Color.LightGray)
                .aspectRatio(ratio = 2f / 3f)
        )
        Text(
            text = name,
            minLines = 2
        )
    }
}

@Composable
fun MovieBackdropImage(
    backdrops: List<Backdrop>,
    backdrop: Backdrop,
    onImageClick: (images: List<String>, clickedIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(backdrop.filePath.toTmdbBackdrop780Url())
            .build(),
        //error = painterResource(R.drawable.ic_broken_image),
        //placeholder = painterResource(R.drawable.loading_img),
        contentDescription = backdrop.filePath,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .background(Color.LightGray)
            .aspectRatio(ratio = backdrop.aspectRatio.toFloat())
            .clickable(
                enabled = true,
                onClick = {
                    val images = backdrops.map { it.filePath.toTmdbBackdrop1280Url() }
                    val clickedIndex = backdrops.indexOf(backdrop)
                    onImageClick(images, clickedIndex)
                }
            )
    )
}

@Composable
fun MovieVideoThumbnailItem(
    key: String,
    name: String,
    onVideoClick: (key: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(
                enabled = true,
                onClick = { onVideoClick(key) }
            )
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(ratio = 16f / 9f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data("https://img.youtube.com/vi/${key}/0.jpg")
                    .build(),
                //error = painterResource(R.drawable.ic_broken_image),
                //placeholder = painterResource(R.drawable.loading_img),
                contentDescription = key,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()
            )
            VideoPlayImage(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = name,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(start = 2.dp, end = 2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailContentPreview() {
    MyMoviesTheme {
        val movieDetail = Dummy.movieDetailGodfather
        MovieDetailContent(
            movieDetail = movieDetail,
            movieCredits = MovieCredits(0, emptyList(), emptyList()),
            movieImages = MovieImages(0, emptyList(), emptyList(), emptyList()),
            movieVideos = MovieVideos(0, emptyList()),
            onImageClick = { _, _ -> },
            onVideoClick = {},
            onPersonClick = {},
            modifier = Modifier
        )
    }
}