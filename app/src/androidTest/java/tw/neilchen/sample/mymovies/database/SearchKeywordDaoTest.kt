package tw.neilchen.sample.mymovies.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchKeywordDaoTest {

    //@get:Rule
    //var instantTaskExecutorRule

    private lateinit var database: AppDatabase
    private lateinit var searchKeywordDao: SearchKeywordDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        searchKeywordDao = database.searchKeywordDao()
    }

    @After
    fun teardown() {
        database.close()
    }
}