package database.enitities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import models.KtorText

@Entity
data class KtorTextEntity(
    @PrimaryKey val text: String
) {
    fun toDomain(): KtorText = KtorText(this.text)
}

fun KtorText.toEntity(): KtorTextEntity = KtorTextEntity(this.text)

@Dao
interface KtorTextDao {
    @Upsert
    suspend fun insert(text: KtorTextEntity)

    @Query("SELECT * FROM KtorTextEntity")
    suspend fun getText(): KtorTextEntity?
}