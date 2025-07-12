package tw.neilchen.sample.mymovies.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tw.neilchen.sample.mymovies.data.SearchKeyword
import java.util.Date

@RunWith(AndroidJUnit4::class)
@SmallTest
class SearchKeywordDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var searchKeywordDao: SearchKeywordDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).build()
        searchKeywordDao = database.searchKeywordDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertSearchKeywords_checkResult() = runTest {
        val keyword = SearchKeyword(1, "superman", Date(0))

        searchKeywordDao.insert(keyword)

        // Check the result (normal style)
        val result = searchKeywordDao.getAll().first().first()
        assertEquals(result, keyword)

        // Check the result again (Turbine + Truth style)
        searchKeywordDao.getAll().test {
            assertThat(awaitItem()[0]).isEqualTo(keyword)
        }
    }

    @Test
    fun insertSearchKeywords_checkOrderByDateDescending() = runTest {
        val supercat = SearchKeyword(id = 1, keyword = "supercat", addedAt = Date(456))
        val superman = SearchKeyword(id = 2, keyword = "superman", addedAt = Date(0))
        val superdog = SearchKeyword(id = 3, keyword = "superdog", addedAt = Date(123))

        searchKeywordDao.insert(superman)
        searchKeywordDao.insert(supercat)
        searchKeywordDao.insert(superdog)

        searchKeywordDao.getAll().test {
            assertThat(awaitItem()).containsExactly(superman, superdog, supercat)
        }
    }

    @Test
    fun insertSearchKeywords_checkUniqueKeywords() = runTest {
        val war = SearchKeyword(keyword = "war", addedAt = Date(1_000))
        val war2 = SearchKeyword(keyword = "war", addedAt = Date(2_000))
        val war3 = SearchKeyword(keyword = "war", addedAt = Date(3_000))

        searchKeywordDao.insert(war)
        searchKeywordDao.insert(war2)
        searchKeywordDao.insert(war3)

        searchKeywordDao.getAll().test {
            val list = awaitItem()
            assertThat(list).hasSize(1)
            assertThat(list[0].keyword).isEqualTo("war")
            assertThat(list[0].addedAt.time).isEqualTo(3_000)
        }
    }

    @Test
    fun insertSearchKeywords_checkOrderByDateWhenInsertTheSameKeywords() = runTest {
        val matrix = SearchKeyword(keyword = "matrix", addedAt = Date(1_000))
        val hero = SearchKeyword(keyword = "hero", addedAt = Date(2_000))
        val transformer = SearchKeyword(keyword = "transformer", addedAt = Date(3_000))
        val matrixAgain = SearchKeyword(keyword = "matrix", addedAt = Date(9_000))

        searchKeywordDao.insert(matrix)
        searchKeywordDao.insert(hero)
        searchKeywordDao.insert(transformer)
        searchKeywordDao.insert(matrixAgain)

        searchKeywordDao.getAll().test {
            val keywords = awaitItem()
            assertThat(keywords).hasSize(3)
            assertThat(keywords[0].keyword).isEqualTo("matrix")
            assertThat(keywords[1].keyword).isEqualTo("transformer")
            assertThat(keywords[2].keyword).isEqualTo("hero")
        }
    }
}