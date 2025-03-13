package tw.neilchen.sample.mymovies.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import tw.neilchen.sample.mymovies.ui.theme.MyMoviesTheme

@Composable
fun ImagesPagerScreen(
    images: List<String>,
    clickedIndex: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState(
            pageCount = { images.size },
            initialPage = clickedIndex
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ImagePagerItem(
                imageUrl = images[page],
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 4.dp, top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        if (images.size > 1) {
            PagerIndicator(
                currentPage = pagerState.currentPage,
                pageCount = images.size,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun ImagePagerItem(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val zoomState = rememberZoomState()
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(imageUrl)
            .build(),
        contentDescription = imageUrl,
        contentScale = ContentScale.Fit,
        modifier = modifier.zoomable(zoomState)
    )
}

@Composable
fun PagerIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = "${currentPage + 1} / $pageCount",
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier
                .background(
                    color = Color(color = 0x80_00_00_00),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(start = 4.dp, end = 4.dp)
        )
    }
}

@Preview
@Composable
private fun ImagePagerScreen() {
    MyMoviesTheme {
        ImagesPagerScreen(
            images = listOf("1", "2", "3", "4", "5"),
            clickedIndex = 2,
            onBackClick = {},
            modifier = Modifier
        )
    }
}