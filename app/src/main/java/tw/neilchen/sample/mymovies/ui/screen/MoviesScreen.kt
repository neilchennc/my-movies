package tw.neilchen.sample.mymovies.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tw.neilchen.sample.mymovies.R
import tw.neilchen.sample.mymovies.data.Dummy
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.ui.common.CircularProgressLoading
import tw.neilchen.sample.mymovies.ui.common.ErrorApiResponseContent
import tw.neilchen.sample.mymovies.ui.common.MovieItem
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme
import tw.neilchen.sample.mymovies.ui.viewmodel.MoviesUiState
import tw.neilchen.sample.mymovies.ui.viewmodel.MoviesViewModel

@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (movie: Movie) -> Unit,
    onBackClick: () -> Unit,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onBackClick()
    }

    when (uiState) {
        is MoviesUiState.Loading -> {
            MoviesLoadingContent(modifier = modifier)
        }

        is MoviesUiState.Success -> {
            val success = uiState as MoviesUiState.Success
            MoviesContent(
                modifier = modifier,
                trendingMovies = success.trendingMovies,
                nowPlayingMovies = success.nowPlayingMovies,
                upcomingMovies = success.upcomingMovies,
                popularMovies = success.popularMovies,
                topRatedMovies = success.topRatedMovies,
                onMovieClick = onMovieClick
            )
        }

        is MoviesUiState.Error -> {
            val error = uiState as MoviesUiState.Error
            MoviesErrorContent(
                modifier = modifier,
                throwable = error.throwable
            )
        }
    }
}

@Composable
fun MoviesLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressLoading(modifier = modifier.align(Alignment.Center))
    }
}

@Composable
fun MoviesErrorContent(modifier: Modifier = Modifier, throwable: Throwable) {
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
fun MoviesContent(
    trendingMovies: List<Movie>,
    nowPlayingMovies: List<Movie>,
    upcomingMovies: List<Movie>,
    popularMovies: List<Movie>,
    topRatedMovies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        CommonBaseMovieList(
            title = stringResource(R.string.trending_day),
            movies = trendingMovies,
            onMovieClick = onMovieClick
        )

        CommonBaseMovieList(
            title = stringResource(R.string.now_playing),
            movies = nowPlayingMovies,
            onMovieClick = onMovieClick
        )

        CommonBaseMovieList(
            title = stringResource(R.string.upcoming),
            movies = upcomingMovies,
            onMovieClick = onMovieClick
        )

        CommonBaseMovieList(
            title = stringResource(R.string.popular),
            movies = popularMovies,
            onMovieClick = onMovieClick
        )

        CommonBaseMovieList(
            title = stringResource(R.string.top_rated),
            movies = topRatedMovies,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
fun CommonBaseMovieList(
    title: String,
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            modifier = Modifier.padding(8.dp)
        )
        MovieList(
            movies = movies,
            onMovieClick = onMovieClick,
            modifier = Modifier
        )
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        items(movies) { movie ->
            MovieItem(movie, onMovieClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingContentPreview() {
    MyMoviesTheme {
        MoviesLoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorContentPreview() {
    MyMoviesTheme {
        MoviesErrorContent(throwable = Throwable("Error Message"))
    }
}

@Preview(showBackground = true)
@Composable
private fun MoviesContentPreview() {
    MyMoviesTheme {
        val movie = Dummy.movieGodfather
        MoviesContent(
            nowPlayingMovies = listOf(movie, movie, movie, movie),
            upcomingMovies = listOf(movie, movie),
            popularMovies = emptyList(),
            topRatedMovies = listOf(movie),
            trendingMovies = listOf(movie, movie, movie, movie, movie, movie, movie),
            onMovieClick = {}
        )
    }
}
