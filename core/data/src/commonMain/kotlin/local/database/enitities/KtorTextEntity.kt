package local.database.enitities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

@Entity(tableName = "ktor_text")
data class KtorTextEntity(
    @PrimaryKey val text: String,
    val lastUpdated: Long = Clock.System.now().toEpochMilliseconds()
)

@Dao
interface KtorTextDao {
    @Upsert
    suspend fun insert(text: KtorTextEntity)

    @Query("SELECT * FROM ktor_text")
    fun getTextFlow(): Flow<KtorTextEntity?>
    @Query("SELECT * FROM ktor_text")
    suspend fun getText(): KtorTextEntity?
}