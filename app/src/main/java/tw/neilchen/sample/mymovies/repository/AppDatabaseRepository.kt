package tw.neilchen.sample.mymovies.repository

import kotlinx.coroutines.flow.Flow
import tw.neilchen.sample.mymovies.data.SearchKeyword
import tw.neilchen.sample.mymovies.database.SearchKeywordDao
import javax.inject.Inject

class AppDatabaseRepository @Inject constructor(
    private val searchKeywordDao: SearchKeywordDao
) : DatabaseRepository {

    override fun getAllSearchKeywords(): Flow<List<SearchKeyword>> {
        return searchKeywordDao.getAll()
    }

    override suspend fun insertSearchKeyword(searchKeyword: SearchKeyword) {
        return searchKeywordDao.insert(searchKeyword)
    }

    override suspend fun deleteSearchKeyword(searchKeyword: SearchKeyword) {
        return searchKeywordDao.delete(searchKeyword)
    }
}