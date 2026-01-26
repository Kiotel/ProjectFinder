package local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import local.database.enitities.KtorTextDao
import local.database.enitities.KtorTextEntity

@Database(entities = [KtorTextEntity::class], version = 3)
abstract class TestDataBase : RoomDatabase() {
    abstract fun KtorTextDao(): KtorTextDao
}