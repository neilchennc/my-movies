package tw.neilchen.sample.mymovies.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import tw.neilchen.sample.mymovies.R
import tw.neilchen.sample.mymovies.data.Movie
import tw.neilchen.sample.mymovies.ui.common.CircularProgressLoading
import tw.neilchen.sample.mymovies.ui.common.ErrorApiResponseContent
import tw.neilchen.sample.mymovies.ui.common.MovieItem
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme
import tw.neilchen.sample.mymovies.ui.viewmodel.SearchMoviesViewModel

@Composable
fun SearchMoviesScreen(
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchMoviesViewModel = hiltViewModel(),
) {
    val movies = viewModel.resultFlow.collectAsLazyPagingItems()
    val searchKeywords by viewModel.keywordsFlow.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBarContent(
            suggestions = searchKeywords,
            onSearch = { keyword ->
                viewModel.insertSearchKeyword(keyword)
                viewModel.searchMovies(keyword)
            },
            modifier = Modifier.fillMaxWidth()
        )
        SearchResultContent(
            movies = movies,
            onMovieClick = onMovieClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarContent(
    suggestions: List<String>,
    onSearch: (keyword: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var keyword by rememberSaveable { mutableStateOf("") }
    var firstLaunched by rememberSaveable { mutableStateOf(true) }
    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (firstLaunched) {
            firstLaunched = false
            dropdownMenuExpanded = true
//            focusRequester.requestFocus()
        }
    }

    ExposedDropdownMenuBox(
        expanded = dropdownMenuExpanded,
        onExpandedChange = { dropdownMenuExpanded = it },
        modifier = modifier
    ) {
        TextField(
            value = keyword,
            placeholder = { Text(stringResource(R.string.search_movie_placeholder)) },
            onValueChange = { keyword = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    dropdownMenuExpanded = false
                    onSearch(keyword)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .menuAnchor(
                    type = MenuAnchorType.PrimaryEditable,
                    enabled = true
                )
                .onFocusChanged {
                    if (it.isFocused) {
//                        dropdownMenuExpanded = true
                        keyboardController?.show()
                    } else {
//                        dropdownMenuExpanded = false
                        keyboardController?.hide()
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
//            focusedPlaceholderColor = Color(0x7F_00_00_00),
//            unfocusedPlaceholderColor = Color(0x7F_00_00_00)
//            selectionColors = TextSelectionColors(
//                handleColor = MaterialTheme.colorScheme.secondary,
//                backgroundColor = MaterialTheme.colorScheme.onPrimary
//            ),
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        dropdownMenuExpanded = false
                        onSearch(keyword)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            }
        )

        ExposedDropdownMenu(
            expanded = dropdownMenuExpanded,
            onDismissRequest = { dropdownMenuExpanded = false }
        ) {
            suggestions
                .filter { it.contains(keyword, ignoreCase = true) }
                .forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(text = suggestion) },
                        onClick = {
                            keyword = suggestion
                            focusManager.clearFocus()
                            dropdownMenuExpanded = false
                            onSearch(suggestion)
                        },
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
                    )
                }
        }
    }
}

@Composable
fun SearchResultContent(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    // First load
    when (movies.loadState.refresh) {
        is LoadState.NotLoading -> {}

        is LoadState.Loading -> {
            LaunchedEffect(Unit) {
                gridState.scrollToItem(0)
            }
            Box(modifier = modifier) {
                CircularProgressLoading(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is LoadState.Error -> {
            Box(modifier = modifier) {
                val throwable = (movies.loadState.refresh as LoadState.Error).error
                ErrorApiResponseContent(throwable = throwable)
            }
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = modifier
    ) {
        items(
            count = movies.itemCount,
            //key = movies.itemKey { it.id }
            key = { it }
        ) { index ->
            movies[index]?.let { movie ->
                MovieItem(
                    movie = movie,
                    onMovieClick = { onMovieClick(movie) }
                )
            }
        }

        // Append data (load more)
        when (movies.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressLoading(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            is LoadState.Error -> {}
            is LoadState.NotLoading -> {}
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    MyMoviesTheme {
        SearchBarContent(
            suggestions = emptyList(),
            onSearch = { _ -> }
        )
    }
}
