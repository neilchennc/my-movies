package tw.neilchen.sample.mymovies.ui

import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import tw.neilchen.sample.mymovies.R
import tw.neilchen.sample.mymovies.data.toYouTubeUrl
import tw.neilchen.sample.mymovies.ui.Routes.Home
import tw.neilchen.sample.mymovies.ui.Routes.ImagesPager
import tw.neilchen.sample.mymovies.ui.Routes.MovieDetail
import tw.neilchen.sample.mymovies.ui.Routes.PersonDetail
import tw.neilchen.sample.mymovies.ui.Routes.SearchMovies
import tw.neilchen.sample.mymovies.ui.screen.ImagesPagerScreen
import tw.neilchen.sample.mymovies.ui.screen.MovieDetailScreen
import tw.neilchen.sample.mymovies.ui.screen.MoviesScreen
import tw.neilchen.sample.mymovies.ui.screen.PersonDetailScreen
import tw.neilchen.sample.mymovies.ui.screen.SearchMoviesScreen
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme
import tw.neilchen.sample.mymovies.ui.util.TestTags

data object Routes {
    @Serializable
    object Home

    @Serializable
    data class MovieDetail(val movieId: Int)

    @Serializable
    data class ImagesPager(val images: List<String>, val clickedIndex: Int)

    @Serializable
    object SearchMovies

    @Serializable
    data class PersonDetail(val personId: Int)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMoviesApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = backStackEntry?.id != null && navController.previousBackStackEntry != null
    val showTopAppBar = !(backStackEntry?.destination?.hasRoute<ImagesPager>() ?: false)
    val showSearchIcon = !(backStackEntry?.destination?.hasRoute<SearchMovies>() ?: false)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    MyMoviesTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (showTopAppBar) {
                    MyMoviesTopAppBar(
                        canNavigateBack = canNavigateBack,
                        showSearchIcon = showSearchIcon,
                        scrollBehavior = scrollBehavior,
                        onNavigateUp = { navController.navigateUp() },
                        onNavigateHome = {
                            navController.popBackStack(
                                route = Home,
                                inclusive = false,
                                saveState = false
                            )
                        },
                        onSearchClick = { navController.navigate(SearchMovies) }
                    )
                }
            }
        ) { innerPadding ->
            MyMoviesNavHost(
                modifier = Modifier
                    .padding(innerPadding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMoviesTopAppBar(
    canNavigateBack: Boolean,
    showSearchIcon: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateUp: () -> Unit,
    onNavigateHome: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = onNavigateUp,
                    modifier = Modifier.testTag(TestTags.NAVIGATION_ICON_BACK)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (showSearchIcon) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
            if (canNavigateBack) {
                IconButton(onClick = onNavigateHome) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun MyMoviesNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Home,
        enterTransition = { customEnterTransaction() },
        exitTransition = { customExitTransaction() },
        popEnterTransition = { customEnterTransaction() },
        popExitTransition = { customExitTransaction() },
        modifier = modifier
    ) {
        composable<Home> {
            MoviesScreen(
                onMovieClick = { movie ->
                    navController.navigate(MovieDetail(movieId = movie.id))
                },
                onBackClick = {
                    context.findActivity()?.finish()
                }
            )
        }

        composable<MovieDetail> { backStackEntry ->
            val movieDetail: MovieDetail = backStackEntry.toRoute()
            MovieDetailScreen(
                movieId = movieDetail.movieId,
                onImageClick = { images, clickedIndex ->
                    navController.navigate(ImagesPager(images, clickedIndex))
                },
                onVideoClick = { key ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, key.toYouTubeUrl().toUri()))
                },
                onPersonClick = { personId ->
                    navController.navigate(PersonDetail(personId))
                }
            )
        }

        composable<ImagesPager> { backStackEntry ->
            val imagesPager = backStackEntry.toRoute<ImagesPager>()
            ImagesPagerScreen(
                images = imagesPager.images,
                clickedIndex = imagesPager.clickedIndex,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<SearchMovies> {
            SearchMoviesScreen(
                onMovieClick = { movie ->
                    navController.navigate(MovieDetail(movieId = movie.id))
                }
            )
        }

        composable<PersonDetail> { backStackEntry ->
            val personDetail = backStackEntry.toRoute<PersonDetail>()
            PersonDetailScreen(
                personId = personDetail.personId,
                onImageClick = { images, clickedIndex ->
                    navController.navigate(ImagesPager(images, clickedIndex))
                },
                onMovieClick = { movie ->
                    navController.navigate(MovieDetail(movieId = movie.id))
                }
            )
        }
    }
}

private fun customEnterTransaction(): EnterTransition {
    //return EnterTransition.None
    return fadeIn(animationSpec = tween(durationMillis = 150))
}

private fun customExitTransaction(): ExitTransition {
    //return ExitTransition.None
    return fadeOut(animationSpec = tween(durationMillis = 150))
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopAppBarPreview() {
    MyMoviesTheme {
        MyMoviesTopAppBar(
            canNavigateBack = true,
            showSearchIcon = true,
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            onNavigateUp = {},
            onNavigateHome = {},
            onSearchClick = {},
            modifier = Modifier
        )
    }
}