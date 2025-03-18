package tw.neilchen.sample.mymovies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tw.neilchen.sample.mymovies.data.Converters
import tw.neilchen.sample.mymovies.data.SearchKeyword

@Database(entities = [SearchKeyword::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchKeywordDao(): SearchKeywordDao
}