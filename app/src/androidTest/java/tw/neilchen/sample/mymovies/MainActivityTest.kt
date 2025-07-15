package tw.neilchen.sample.mymovies

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tw.neilchen.sample.mymovies.di.AppModule

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    //@get:Rule
    //val composeRule = createAndroidComposeRule<MainActivityTest>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun start() {
        // TODO
    }
}