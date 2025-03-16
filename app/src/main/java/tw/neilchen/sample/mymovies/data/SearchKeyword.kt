package tw.neilchen.sample.mymovies.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "search_keywords")
data class SearchKeyword(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "keyword") val keyword: String,
    @ColumnInfo(name = "added_at") val addedAt: Date
)