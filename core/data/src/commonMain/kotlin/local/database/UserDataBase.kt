package local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import local.database.entities.UserDao
import local.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 5)
@ConstructedBy(UserDataBaseConstructor::class)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
}


@Suppress("KotlinNoActualForExpect")
expect object UserDataBaseConstructor : RoomDatabaseConstructor<UserDataBase> {
    override fun initialize(): UserDataBase
}
