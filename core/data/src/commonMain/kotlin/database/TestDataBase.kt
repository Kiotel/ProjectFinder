package database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import database.enitities.KtorTextDao
import database.enitities.KtorTextEntity

@Database(entities = [KtorTextEntity::class], version = 1)
abstract class TestDataBase : RoomDatabase() {
    abstract fun KtorTextDao(): KtorTextDao
}