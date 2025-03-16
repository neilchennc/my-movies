package tw.neilchen.sample.mymovies.repository

import kotlinx.coroutines.flow.Flow
import tw.neilchen.sample.mymovies.data.SearchKeyword

interface DatabaseRepository {

    fun getAllSearchKeywords(): Flow<List<SearchKeyword>>

    suspend fun insertSearchKeyword(searchKeyword: SearchKeyword)

    suspend fun deleteSearchKeyword(searchKeyword: SearchKeyword)
}