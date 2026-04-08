package local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

@Entity(tableName = "projects")
internal data class ProjectEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "author_id") val authorId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "brief_description") val briefDescription: String,
    @ColumnInfo(name = "stage") val stage: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "viewsCount") val viewsCount: Int,
    @ColumnInfo(name = "likesCount") val likesCount: Int,
    @ColumnInfo(name = "tags") val tags: List<String>,
    @ColumnInfo(name = "needed_roles") val neededRoles: List<String>,
    @ColumnInfo(name = "author_name") val authorName: String,
    @ColumnInfo(name = "last_fetched") val lastFetched: Long
)

@Dao
internal interface ProjectsDao {
    @Upsert
    suspend fun upsert(user: ProjectEntity)

    @Delete
    suspend fun delete(user: ProjectEntity)

    @Query("SELECT * FROM projects LIMIT :limit OFFSET :offset")
    suspend fun getPaged(offset: Int, limit: Int): List<ProjectEntity>
}