package tw.neilchen.sample.mymovies.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tw.neilchen.sample.mymovies.MainActivity
import tw.neilchen.sample.mymovies.R
import tw.neilchen.sample.mymovies.di.AppModule
import tw.neilchen.sample.mymovies.ui.util.TestTags

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MyMoviesAppTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            MyMoviesApp()
        }
    }

    @Test
    fun checkMoviesLit_isLoaded() {
        val trendingText = composeRule.activity.getString(R.string.trending_day)

        with(composeRule) {
            waitUntil(timeoutMillis = 5_000) {
                onAllNodesWithText(trendingText).fetchSemanticsNodes().isNotEmpty()
            }

            onNodeWithText(trendingText).isDisplayed()

            onNodeWithTag(TestTags.MY_MOVIE_LIST).performTouchInput { swipeUp() }

            onNodeWithTag(TestTags.MY_MOVIE_LIST).performTouchInput { swipeDown() }

            onNodeWithText(trendingText).isDisplayed()

            onAllNodesWithTag(TestTags.MOVIE_ITEM)[0].performClick()

            waitUntil(timeoutMillis = 5_000) {
                onAllNodesWithTag(TestTags.MOVIE_BACKDROP_IMAGE).fetchSemanticsNodes().isNotEmpty()
            }

            onNodeWithTag(TestTags.MOVIE_DETAIL_CONTENT).performScrollToNode(hasTestTag(TestTags.MOVIE_POSTER_IMAGE))

            onNodeWithTag(TestTags.MOVIE_POSTER_IMAGE).performClick()

            onNodeWithTag(TestTags.NAVIGATION_ICON_BACK).performClick()

            onNodeWithTag(TestTags.MOVIE_DETAIL_CONTENT).performTouchInput { swipeUp() }

            onNodeWithTag(TestTags.MOVIE_VIDEO_CONTENT).performTouchInput { swipeDown() }

            onNodeWithTag(TestTags.NAVIGATION_ICON_BACK).performClick()

            onNodeWithTag(TestTags.NAVIGATION_ICON_BACK).isNotDisplayed()
        }
    }
}