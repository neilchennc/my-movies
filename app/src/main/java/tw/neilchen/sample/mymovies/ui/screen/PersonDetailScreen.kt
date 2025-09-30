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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import tw.neilchen.sample.mymovies.R
import tw.neilchen.sample.mymovies.data.Dummy
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.data.PersonDetail
import tw.neilchen.sample.mymovies.data.PersonImages
import tw.neilchen.sample.mymovies.data.PersonMovieCredits
import tw.neilchen.sample.mymovies.data.ProfileImage
import tw.neilchen.sample.mymovies.data.toTmdbProfileOriginalUrl
import tw.neilchen.sample.mymovies.data.toTmdbProfileUrl
import tw.neilchen.sample.mymovies.ui.common.CircularProgressLoading
import tw.neilchen.sample.mymovies.ui.common.ErrorApiResponseContent
import tw.neilchen.sample.mymovies.ui.common.HyperlinkText
import tw.neilchen.sample.mymovies.ui.common.MovieItem
import tw.neilchen.sample.mymovies.ui.common.SubtitleText
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme
import tw.neilchen.sample.mymovies.ui.viewmodel.PersonDetailsUiState
import tw.neilchen.sample.mymovies.ui.viewmodel.PersonDetailsViewModel

@Composable
fun PersonDetailScreen(
    personId: Int,
    onImageClick: (images: List<String>, clickedIndex: Int) -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PersonDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadPersonDetail(personId)
    }

    when (uiState) {
        is PersonDetailsUiState.Loading -> {
            PersonDetailLoadingContent(modifier = modifier)
        }

        is PersonDetailsUiState.Success -> {
            val success = uiState as PersonDetailsUiState.Success
            PersonDetailContent(
                personDetail = success.personDetail,
                personImages = success.personImages,
                personMovieCredits = success.personMovieCredits,
                onImageClick = onImageClick,
                onMovieClick = onMovieClick,
                modifier = modifier
            )
        }

        is PersonDetailsUiState.Error -> {
            val error = uiState as PersonDetailsUiState.Error
            PersonDetailErrorContent(modifier = modifier, throwable = error.throwable)
        }
    }
}

@Composable
fun PersonDetailLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressLoading(modifier = modifier.align(Alignment.Center))
    }
}

@Composable
fun PersonDetailErrorContent(modifier: Modifier = Modifier, throwable: Throwable) {
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
fun PersonDetailContent(
    personDetail: PersonDetail,
    personImages: PersonImages,
    personMovieCredits: PersonMovieCredits,
    onImageClick: (images: List<String>, clickedIndex: Int) -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PersonDetailsRow(
            personDetail = personDetail,
            modifier = Modifier.padding(8.dp)
        )

        PersonBiographyContent(
            biography = personDetail.biography,
            modifier = Modifier
        )

        ProfileImagesContent(
            personImages = personImages,
            onImageClick = onImageClick,
            modifier = Modifier
        )

        PersonCastMovieList(
            movies = personMovieCredits.cast,
            onMovieClick = onMovieClick,
            modifier = Modifier
        )

        PersonCrewMovieList(
            movies = personMovieCredits.crew,
            onMovieClick = onMovieClick,
            modifier = Modifier
        )
    }
}

@Composable
fun PersonDetailsRow(
    personDetail: PersonDetail,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        PersonImageItem(
            imageUrl = personDetail.profilePath.toTmdbProfileUrl(),
            modifier = Modifier
                .width(120.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        PersonIntroduction(
            personDetail = personDetail,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PersonImageItem(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(imageUrl)
            .build(),
        //error = painterResource(R.drawable.ic_broken_image),
        //placeholder = painterResource(R.drawable.loading_img),
        contentDescription = "person image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .background(Color.LightGray)
            .aspectRatio(ratio = 2f / 3f)
    )
}

@Composable
fun PersonIntroduction(
    personDetail: PersonDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = personDetail.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (personDetail.birthday != null) {
            Text(text = "${stringResource(R.string.person_birthday)}: ${personDetail.birthday}")
        } else {
            Text(text = "${stringResource(R.string.person_birthday)}: --")
        }

        if (personDetail.deathday != null) {
            Text(text = "${stringResource(R.string.person_deathday)}: ${personDetail.deathday}")
        } else {
            Text(text = "${stringResource(R.string.person_deathday)}: --")
        }

        val personGender = when (personDetail.gender) {
            1 -> stringResource(R.string.person_gender_female)
            2 -> stringResource(R.string.person_gender_male)
            3 -> stringResource(R.string.person_gender_non_binary)
            else -> stringResource(R.string.person_gender_not_set)
        }
        Text(text = "${stringResource(R.string.person_gender)}: $personGender")

        if (personDetail.placeOfBirth != null) {
            Text(text = "${stringResource(R.string.person_place_of_birth)}: ${personDetail.placeOfBirth}")
        } else {
            Text(text = "${stringResource(R.string.person_place_of_birth)}: --")
        }

        Row {
            Text(text = "IMDB ID: ")
            if (personDetail.imdbId != null) {
                HyperlinkText(
                    text = personDetail.imdbId,
                    url = "https://www.imdb.com/name/${personDetail.imdbId}/"
                )
            } else {
                Text(text = "--")
            }
        }
    }
}

@Composable
fun PersonBiographyContent(
    biography: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubtitleText(
            title = stringResource(R.string.person_biography),
            modifier = Modifier.padding(8.dp)
        )
        if (biography != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = biography,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun ProfileImagesContent(
    personImages: PersonImages,
    onImageClick: (images: List<String>, clickedIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubtitleText(
            title = stringResource(R.string.person_images),
            modifier = Modifier.padding(8.dp)
        )

        val profiles = personImages.profiles
        LazyRow(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        ) {
            items(profiles) { profile ->
                ProfileImageItem(
                    profileImage = profile,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            enabled = true,
                            onClick = {
                                val images = profiles.map { it.filePath.toTmdbProfileOriginalUrl() }
                                val clickedIndex = profiles.indexOf(profile)
                                onImageClick(images, clickedIndex)
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun ProfileImageItem(
    profileImage: ProfileImage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(92.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(profileImage.filePath.toTmdbProfileUrl())
                .build(),
            //error = painterResource(R.drawable.ic_broken_image),
            //placeholder = painterResource(R.drawable.loading_img),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .background(Color.LightGray)
                .aspectRatio(ratio = profileImage.aspectRatio)
        )
    }
}

@Composable
fun PersonCastMovieList(
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubtitleText(
            title = stringResource(R.string.person_cast_movie),
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        ) {
            items(movies) { movie ->
                MovieItem(
                    movie = movie,
                    onMovieClick = onMovieClick,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun PersonCrewMovieList(
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubtitleText(
            title = stringResource(R.string.person_crew_movie),
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        ) {
            items(movies) { movie ->
                MovieItem(
                    movie = movie,
                    onMovieClick = onMovieClick,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
private fun PersonDetailContentPreview() {
    MyMoviesTheme {
        PersonDetailContent(
            personDetail = Dummy.personDetail,
            personImages = PersonImages(
                id = 3084,
                profiles = listOf(ProfileImage(0.667f, 0, "", "", 0.0, 0, 0))
//                profiles = emptyList()
            ),
            personMovieCredits = PersonMovieCredits(
                id = 3084,
                cast = listOf(Dummy.movieGodfather),
                crew = listOf(Dummy.movieGodfather)
            ),
            onImageClick = { _, _ -> },
            onMovieClick = {},
        )
    }
}