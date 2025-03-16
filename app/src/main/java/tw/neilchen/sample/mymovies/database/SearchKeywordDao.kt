package tw.neilchen.sample.mymovies.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tw.neilchen.sample.mymovies.data.SearchKeyword

@Dao
interface SearchKeywordDao {
    @Query("SELECT * FROM search_keywords ORDER BY added_at DESC")
    fun getAll(): Flow<List<SearchKeyword>>

    @Insert
    suspend fun insert(searchKeyword: SearchKeyword)

    @Delete
    suspend fun delete(searchKeyword: SearchKeyword)
}