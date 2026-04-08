package local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user")
internal data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "full_name") val fullName: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long
)

@Dao
internal interface UserDao {
    @Upsert
    suspend fun upsert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun getAsFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun get(): UserEntity?
}