package tw.neilchen.sample.mymovies.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * The entity representing the user's search history.
 */
@Entity(tableName = "search_keywords", indices = [Index(value = ["keyword"], unique = true)])
data class SearchKeyword(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "keyword") val keyword: String,
    @ColumnInfo(name = "added_at") val addedAt: Date
)