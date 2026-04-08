package local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSHomeDirectory

internal fun getUserDataBase(): RoomDatabase.Builder<UserDataBase> {
    val dbFile = NSHomeDirectory() + "/user.db"
    return Room.databaseBuilder<UserDataBase>(
        name = dbFile,
        // factory = { TestDataBase::class.instantiateImpl() }
    )
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}